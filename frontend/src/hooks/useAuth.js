import { useState, useEffect } from 'react';
import { isTokenExpired } from '../utils/tokenUtils'; // Импорт функции из tokenUtils.js

const useAuth = () => {
    const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken'));

    useEffect(() => {
        if (isTokenExpired(accessToken)) {
            // Здесь можно вызвать функцию обновления токена, если у вас есть refresh-токен
            // refreshAccessToken().then(newToken => setAccessToken(newToken));
            setAccessToken(null);
            localStorage.removeItem('accessToken');
        }
    }, [accessToken]);

    const fetchWithAuth = async (url, options = {}) => {
        if (!accessToken || isTokenExpired(accessToken)) {
            setAccessToken(null);
            throw new Error('Access token is expired or missing');
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

    return { accessToken, fetchWithAuth };
};

export default useAuth;