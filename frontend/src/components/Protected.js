// components/Protected.js
import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../contexts/AuthContext';

const Protected = () => {
    const { axiosInstance, loading, error: authError } = useContext(AuthContext);
    const [responseData, setResponseData] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProtectedData = async () => {
            try {
                const res = await axiosInstance.get('/users/protected');
                setResponseData(res.data.response);
            } catch (err) {
                if (axios.isCancel(err)) {
                    console.log('Запрос был отменен:', err.message);
                } else {
                    setError('Произошла ошибка: ' + (err.response?.data?.message || err.message));
                }
            }
        };

        if (!loading && !authError) {
            fetchProtectedData();
        }
    }, [axiosInstance, loading, authError]);

    if (loading) {
        return null; // Или индикатор загрузки
    }

    return (
        <div>
            <h1>Защищенная Страница</h1>
            {authError ? (
                <p>Ошибка: {authError}</p>
            ) : error ? (
                <p>Ошибка: {error}</p>
            ) : (
                <p>{responseData}</p>
            )}
        </div>
    );
};

export default Protected;
