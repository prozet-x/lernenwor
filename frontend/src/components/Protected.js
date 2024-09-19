import React, { useState, useEffect } from 'react';
import useAuth from '../hooks/useAuth';

const ProtectedResource = () => {
    const { axiosInstance, loading, error: authError, isRefreshingTokens } = useAuth(); // Добавили isRefreshingTokens
    const [response, setResponse] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchResource = async () => {
            try {
                const res = await axiosInstance.get('/api/v1/users/protected');
                setResponse(res.data.response);
            } catch (err) {
                setError('Произошла ошибка: ' + err.message);
            }
        };

        // Выполняем запрос только если не идет загрузка, нет ошибки аутентификации и не идет обновление токенов
        if (!loading && !authError && !isRefreshingTokens) {
            fetchResource();
        }
    }, [axiosInstance, loading, authError, isRefreshingTokens]); // Добавили isRefreshingTokens в зависимости

    if (loading || isRefreshingTokens) { // Добавили проверку на обновление токенов
        return <p>Загрузка...</p>;
    }

    return (
        <div>
            {authError ? (
                <p>Ошибка: {authError}</p>
            ) : error ? (
                <p>Ошибка: {error}</p>
            ) : (
                <p>{response}</p>
            )}
        </div>
    );
};

export default ProtectedResource;
