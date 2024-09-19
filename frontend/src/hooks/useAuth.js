import { useState, useEffect, useCallback, useRef, useMemo } from 'react';
import { isTokenExpired } from '../utils/tokenUtils';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const useAuth = () => {
    const [accessToken, setAccessToken] = useState(null);
    const accessTokenRef = useRef(null);
    const [loading, setLoading] = useState(true); // Отображение загрузки для проверки токенов
    const [error, setError] = useState(null);
    const refreshTokenPromiseRef = useRef(null); // Для контроля promise обновления токенов
    const [isRefreshingTokens, setIsRefreshingTokens] = useState(false); // Добавляем состояние для контроля обновления токенов
    const navigate = useNavigate();

    // Создание axiosInstance
    const axiosInstance = useMemo(() => axios.create(), []);

    // Обновление ref токена при изменении accessToken
    useEffect(() => {
        accessTokenRef.current = accessToken;
    }, [accessToken]);

    // Функция обновления токенов
    const updateTokens = useCallback(async () => {
        setIsRefreshingTokens(true); // Устанавливаем состояние обновления токенов
        try {
            const response = await axiosInstance.get('/api/v1/auth/updateTokens', { withCredentials: true });
            const newAccessToken = response.data.accessToken;
            if (newAccessToken) {
                setAccessToken(newAccessToken);
                accessTokenRef.current = newAccessToken;
                setError(null);
            }
        } catch (err) {
            console.error('Не удалось обновить токены', err);
            setAccessToken(null);
            accessTokenRef.current = null;
            setError('Не удалось обновить токены');
            navigate('/signin');
        } finally {
            setIsRefreshingTokens(false); // Завершаем состояние обновления токенов
        }
    }, [axiosInstance, navigate]);

    // Настройка интерцепторов для axios
    useEffect(() => {
        const requestInterceptor = axiosInstance.interceptors.request.use(
            (config) => {
                if (isRefreshingTokens || !accessTokenRef.current) {
                    throw new axios.Cancel('Access token is missing or being refreshed');
                }
                config.headers['Authorization'] = `Bearer ${accessTokenRef.current}`;
                return config;
            },
            (error) => Promise.reject(error)
        );

        const responseInterceptor = axiosInstance.interceptors.response.use(
            (response) => response,
            async (error) => {
                const originalRequest = error.config;

                if (error.response && error.response.status === 401 && !originalRequest._retry) {
                    originalRequest._retry = true;

                    if (!refreshTokenPromiseRef.current) {
                        refreshTokenPromiseRef.current = updateTokens();
                    }

                    await refreshTokenPromiseRef.current;
                    refreshTokenPromiseRef.current = null;

                    if (accessTokenRef.current) {
                        originalRequest.headers['Authorization'] = `Bearer ${accessTokenRef.current}`;
                        return axiosInstance(originalRequest);
                    } else {
                        navigate('/signin');
                        return Promise.reject(error);
                    }
                }

                return Promise.reject(error);
            }
        );

        // Удаляем интерцепторы при размонтировании
        return () => {
            axiosInstance.interceptors.request.eject(requestInterceptor);
            axiosInstance.interceptors.response.eject(responseInterceptor);
        };
    }, [axiosInstance, updateTokens, isRefreshingTokens, navigate]);

    // Инициализация аутентификации
    useEffect(() => {
        const initializeAuth = async () => {
            if (!accessToken || isTokenExpired(accessToken)) {
                if (!refreshTokenPromiseRef.current) {
                    refreshTokenPromiseRef.current = updateTokens();
                }
                await refreshTokenPromiseRef.current;
                refreshTokenPromiseRef.current = null;
            }
            setLoading(false);
        };
        initializeAuth();
    }, [accessToken, updateTokens]);

    return { accessToken, axiosInstance, error, loading, isRefreshingTokens }; // Возвращаем isRefreshingTokens для использования в компоненте
};

export default useAuth;
