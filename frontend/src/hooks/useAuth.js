// hooks/useAuth.js
import { useState, useEffect, useCallback, useRef, useMemo } from 'react';
import { isTokenExpired } from '../utils/tokenUtils';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const useAuth = () => {
    const [accessToken, setAccessToken] = useState(null);
    const accessTokenRef = useRef(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const refreshTokenPromiseRef = useRef(null);
    const [isRefreshingTokens, setIsRefreshingTokens] = useState(false);
    const navigate = useNavigate();

    const axiosInstance = useMemo(() => axios.create({
        withCredentials: true,
    }), []);

    useEffect(() => {
        accessTokenRef.current = accessToken;
    }, [accessToken]);

    const updateTokens = useCallback(async () => {
        setIsRefreshingTokens(true);
        try {
            const response = await axiosInstance.get('/api/v1/auth/updateTokens');
            const newAccessToken = response.data.accessToken;
            if (newAccessToken) {
                setAccessToken(newAccessToken);
                accessTokenRef.current = newAccessToken;
                setError(null);
            } else {
                throw new Error('Не получен новый access token');
            }
        } catch (err) {
            console.error('Не удалось обновить токены', err);
            setAccessToken(null);
            accessTokenRef.current = null;
            setError('Не удалось обновить токены');
            navigate('/signin');
        } finally {
            setIsRefreshingTokens(false);
        }
    }, [axiosInstance, navigate]);

    useEffect(() => {
        const requestInterceptor = axiosInstance.interceptors.request.use(
            (config) => {
                if (config.url.includes('/api/v1/auth/updateTokens')) {
                    return config;
                }

                if (isRefreshingTokens || !accessTokenRef.current) {
                    return Promise.reject(new axios.Cancel('Access token is missing or being refreshed'));
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
                if (originalRequest.url.includes('/api/v1/auth/updateTokens')) {
                    return Promise.reject(error);
                }

                if (error.response && error.response.status === 401 && !originalRequest._retry) {
                    originalRequest._retry = true;

                    if (!refreshTokenPromiseRef.current) {
                        refreshTokenPromiseRef.current = updateTokens();
                    }

                    try {
                        await refreshTokenPromiseRef.current;
                        refreshTokenPromiseRef.current = null;

                        if (accessTokenRef.current) {
                            originalRequest.headers['Authorization'] = `Bearer ${accessTokenRef.current}`;
                            return axiosInstance(originalRequest);
                        } else {
                            navigate('/signin');
                            return Promise.reject(error);
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
    }, [axiosInstance, updateTokens, navigate, isRefreshingTokens]);

    useEffect(() => {
        const initializeAuth = async () => {
            try {
                if (!accessTokenRef.current || isTokenExpired(accessTokenRef.current)) {
                    if (!refreshTokenPromiseRef.current) {
                        refreshTokenPromiseRef.current = updateTokens();
                    }
                    await refreshTokenPromiseRef.current;
                    refreshTokenPromiseRef.current = null;
                }
            } catch (err) {
                console.error('Ошибка инициализации аутентификации', err);
            } finally {
                setLoading(false);
            }
        };
        initializeAuth();
    }, [updateTokens]);

    return { accessToken, axiosInstance, error, loading, isRefreshingTokens };
};

export default useAuth;
