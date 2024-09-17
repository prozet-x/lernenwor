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
    const navigate = useNavigate();

    // Мемоизация axiosInstance
    const axiosInstance = useMemo(() => axios.create(), []);

    // Синхронизация ref с состоянием
    useEffect(() => {
        accessTokenRef.current = accessToken;
    }, [accessToken]);

    // Функция обновления токенов
    const updateTokens = useCallback(async () => {
        try {
            const response = await axiosInstance.get('/api/v1/auth/updateTokens', { withCredentials: true });
            const newAccessToken = response.data.accessToken;
            setAccessToken(newAccessToken);
            accessTokenRef.current = newAccessToken;
            setError(null);
        } catch (err) {
            console.error('Не удалось обновить токены', err);
            setAccessToken(null);
            accessTokenRef.current = null;
            setError('Не удалось обновить токены');
            navigate('/signin');
        }
    }, [axiosInstance, navigate]);

    // Настройка интерцепторов axios
    useEffect(() => {
        // Интерцептор запросов
        const requestInterceptor = axiosInstance.interceptors.request.use(
            (config) => {
                if (accessTokenRef.current) {
                    config.headers['Authorization'] = `Bearer ${accessTokenRef.current}`;
                }
                return config;
            },
            (error) => Promise.reject(error)
        );

        // Интерцептор ответов
        const responseInterceptor = axiosInstance.interceptors.response.use(
            (response) => {
                // Проверка нового access-токена в заголовках ответа
                const newAccessToken = response.headers['authorization']?.split(' ')[1];
                if (newAccessToken) {
                    setAccessToken(newAccessToken);
                    accessTokenRef.current = newAccessToken;
                }
                return response;
            },
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

        // Функция очистки для удаления интерцепторов
        return () => {
            axiosInstance.interceptors.request.eject(requestInterceptor);
            axiosInstance.interceptors.response.eject(responseInterceptor);
        };
    }, [axiosInstance, updateTokens, navigate]);

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
    }, [accessToken, updateTokens, axiosInstance]);

    return { accessToken, axiosInstance, error, loading };
};

export default useAuth;




// import { useState, useEffect, useCallback } from 'react';
// import { isTokenExpired } from '../utils/tokenUtils';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
//
// const useAuth = () => {
//     const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken'));
//     const [loading, setLoading] = useState(true);
//     const [error, setError] = useState(null);
//     const [tokenRefreshing, setTokenRefreshing] = useState(false);
//     const [refreshTokenPromise, setRefreshTokenPromise] = useState(null);
//     const navigate = useNavigate();
//
//     // Перехватчик ответов
//     axios.interceptors.response.use(
//         (response) => {
//             // Проверяем наличие нового access-токена в заголовках
//             const newAccessToken = response.headers['authorization']?.split(' ')[1];
//             if (newAccessToken) {
//                 localStorage.setItem('accessToken', newAccessToken);
//                 setAccessToken(newAccessToken);
//             }
//             return response;
//         },
//         (error) => {
//             return Promise.reject(error);
//         }
//     );
//
//     const updateTokens = useCallback(async () => {
//         setTokenRefreshing(true);
//         try {
//             const response = await axios.get('/api/v1/auth/updateTokens', { withCredentials: true });
//             // Обновление токена из данных ответа (если сервер возвращает его в теле ответа)
//             const newAccessToken = response.data.accessToken;
//             localStorage.setItem('accessToken', newAccessToken);
//             setAccessToken(newAccessToken);
//             setError(null);
//         } catch (err) {
//             console.error('Failed to refresh tokens', err);
//             setAccessToken(null);
//             localStorage.removeItem('accessToken');
//             setError('Failed to refresh tokens');
//             navigate('/signin');
//         } finally {
//             setTokenRefreshing(false);
//             setRefreshTokenPromise(null);
//         }
//     }, [navigate]);
//
//     useEffect(() => {
//         const initializeAuth = async () => {
//             if (!accessToken || isTokenExpired(accessToken)) {
//                 if (!refreshTokenPromise) {
//                     const promise = updateTokens();
//                     setRefreshTokenPromise(promise);
//                     await promise;
//                 } else {
//                     await refreshTokenPromise;
//                 }
//             }
//             setLoading(false);
//         };
//         initializeAuth();
//     }, [accessToken, refreshTokenPromise, updateTokens]);
//
//     const fetchWithAuth = async (url, options = {}) => {
//         if (tokenRefreshing) {
//             await refreshTokenPromise;
//         }
//
//         if (!accessToken || isTokenExpired(accessToken)) {
//             if (!refreshTokenPromise) {
//                 await updateTokens();
//             } else {
//                 await refreshTokenPromise;
//             }
//
//             if (!accessToken) {
//                 throw new Error('Access token is expired or missing');
//             }
//         }
//
//         const response = await fetch(url, {
//             ...options,
//             headers: {
//                 ...options.headers,
//                 'Authorization': `Bearer ${accessToken}`,
//             },
//         });
//
//         return response;
//     };
//
//     return { accessToken, fetchWithAuth, error, loading };
// };
//
// export default useAuth;
