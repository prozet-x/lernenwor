// contexts/AuthContext.js
import React, { createContext, useState, useEffect, useRef, useCallback, useMemo } from 'react';
import axios from 'axios';
import { isTokenExpired } from '../utils/tokenUtils';
import { useNavigate } from 'react-router-dom';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [accessToken, setAccessToken] = useState(null); // Храним токен в состоянии
    const accessTokenRef = useRef(accessToken);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const refreshTokenPromiseRef = useRef(null);
    const navigate = useNavigate();

    // Создаём axios экземпляр
    const axiosInstance = useMemo(() => axios.create({
        baseURL: '/api/v1',
        withCredentials: true, // Чтобы отправлять HttpOnly куки
    }), []);

    // Обновляем ref при изменении accessToken
    useEffect(() => {
        accessTokenRef.current = accessToken;
    }, [accessToken]);

    // Очередь запросов, ожидающих обновления токенов
    const requestQueue = useRef([]);

    // Функция для обработки очереди
    const processQueue = (error, token = null) => {
        requestQueue.current.forEach(prom => {
            if (error) {
                prom.reject(error);
            } else {
                prom.resolve(token);
            }
        });
        requestQueue.current = [];
    };

    // Функция обновления токенов
    const updateTokens = useCallback(async () => {
        try {
            const response = await axiosInstance.get('/auth/updateTokens');
            const newAccessToken = response.data.accessToken;
            if (newAccessToken) {
                console.log('Новый accessToken:', newAccessToken);
                setAccessToken(newAccessToken);
                setError(null);
                processQueue(null, newAccessToken);
                return newAccessToken;
            } else {
                throw new Error('Не получен новый access token');
            }
        } catch (err) {
            console.error('Не удалось обновить токены', err);
            setAccessToken(null);
            setError('Не удалось обновить токены');
            processQueue(err, null);
            navigate('/signin');
            throw err;
        }
    }, [axiosInstance, navigate]);

    // Настройка interceptors
    useEffect(() => {
        // Request interceptor
        const requestInterceptor = axiosInstance.interceptors.request.use(
            (config) => {
                // Не добавляем заголовок для эндпоинта обновления токенов или если указано skipAuth
                if (config.url.includes('/auth/updateTokens') || config.skipAuth) {
                    return config;
                }

                if (accessTokenRef.current) {
                    config.headers['Authorization'] = `Bearer ${accessTokenRef.current}`;
                    console.log('Добавлен Authorization заголовок:', config.headers['Authorization']);
                }
                return config;
            },
            (error) => {
                console.error('Ошибка запроса:', error);
                return Promise.reject(error);
            }
        );

        // Response interceptor
        const responseInterceptor = axiosInstance.interceptors.response.use(
            (response) => response,
            async (error) => {
                const originalRequest = error.config;

                // Если запрос уже был повторен, отклоняем ошибку
                if (originalRequest._retry) {
                    return Promise.reject(error);
                }

                // Если ошибка 401, пытаемся обновить токены
                if (error.response && error.response.status === 401) {
                    originalRequest._retry = true;

                    if (!refreshTokenPromiseRef.current) {
                        refreshTokenPromiseRef.current = updateTokens();
                    }

                    try {
                        const newToken = await refreshTokenPromiseRef.current;
                        refreshTokenPromiseRef.current = null;
                        if (newToken) {
                            originalRequest.headers['Authorization'] = `Bearer ${newToken}`;
                            return axiosInstance(originalRequest);
                        }
                    } catch (err) {
                        return Promise.reject(err);
                    }
                }

                return Promise.reject(error);
            }
        );

        return () => {
            axiosInstance.interceptors.request.eject(requestInterceptor);
            axiosInstance.interceptors.response.eject(responseInterceptor);
        };
    }, [axiosInstance, updateTokens]);

    // Инициализация аутентификации при загрузке приложения
    useEffect(() => {
        const initializeAuth = async () => {
            try {
                if (!accessTokenRef.current || isTokenExpired(accessTokenRef.current)) {
                    await updateTokens();
                }
            } catch (err) {
                console.error('Ошибка инициализации аутентификации', err);
            } finally {
                setLoading(false);
            }
        };
        initializeAuth();
    }, [updateTokens]);

    if (loading) {
        // Можно вернуть спиннер или другой индикатор загрузки
        return null;
    }

    return (
        <AuthContext.Provider value={{ accessToken, setAccessToken, axiosInstance, error, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
