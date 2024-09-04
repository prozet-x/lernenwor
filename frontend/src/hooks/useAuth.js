import { useState, useEffect, useCallback } from 'react';
import { isTokenExpired } from '../utils/tokenUtils';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const useAuth = () => {
    const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken'));
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [tokenRefreshing, setTokenRefreshing] = useState(false);
    const [refreshTokenPromise, setRefreshTokenPromise] = useState(null);
    const navigate = useNavigate();

    // Перехватчик ответов
    axios.interceptors.response.use(
        (response) => {
            // Проверяем наличие нового access-токена в заголовках
            const newAccessToken = response.headers['authorization']?.split(' ')[1];
            if (newAccessToken) {
                localStorage.setItem('accessToken', newAccessToken);
                setAccessToken(newAccessToken);
            }
            return response;
        },
        (error) => {
            return Promise.reject(error);
        }
    );

    const updateTokens = useCallback(async () => {
        setTokenRefreshing(true);
        try {
            const response = await axios.get('/api/v1/auth/updateTokens', { withCredentials: true });
            // Обновление токена из данных ответа (если сервер возвращает его в теле ответа)
            const newAccessToken = response.data.accessToken;
            localStorage.setItem('accessToken', newAccessToken);
            setAccessToken(newAccessToken);
            setError(null);
        } catch (err) {
            console.error('Failed to refresh tokens', err);
            setAccessToken(null);
            localStorage.removeItem('accessToken');
            setError('Failed to refresh tokens');
            navigate('/signin');
        } finally {
            setTokenRefreshing(false);
            setRefreshTokenPromise(null);
        }
    }, [navigate]);

    useEffect(() => {
        const initializeAuth = async () => {
            if (!accessToken || isTokenExpired(accessToken)) {
                if (!refreshTokenPromise) {
                    const promise = updateTokens();
                    setRefreshTokenPromise(promise);
                    await promise;
                } else {
                    await refreshTokenPromise;
                }
            }
            setLoading(false);
        };
        initializeAuth();
    }, [accessToken, refreshTokenPromise, updateTokens]);

    const fetchWithAuth = async (url, options = {}) => {
        if (tokenRefreshing) {
            await refreshTokenPromise;
        }

        if (!accessToken || isTokenExpired(accessToken)) {
            if (!refreshTokenPromise) {
                await updateTokens();
            } else {
                await refreshTokenPromise;
            }

            if (!accessToken) {
                throw new Error('Access token is expired or missing');
            }
        }

        const response = await fetch(url, {
            ...options,
            headers: {
                ...options.headers,
                'Authorization': `Bearer ${accessToken}`,
            },
        });

        return response;
    };

    return { accessToken, fetchWithAuth, error, loading };
};

export default useAuth;

// import { useState, useEffect, useCallback } from 'react';
// import { isTokenExpired } from '../utils/tokenUtils';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
//
// const useAuth = () => {
//     const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken'));
//     const [loading, setLoading] = useState(true); // Изначально ставим загрузку в true
//     const [error, setError] = useState(null);
//     const [tokenRefreshing, setTokenRefreshing] = useState(false);
//     const [refreshTokenPromise, setRefreshTokenPromise] = useState(null);
//     const navigate = useNavigate();
//
//     const updateTokens = useCallback(async () => {
//         setTokenRefreshing(true);
//         try {
//             const response = await axios.get('/api/v1/auth/updateTokens');
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
//             setLoading(false); // Отключаем загрузку после инициализации
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
