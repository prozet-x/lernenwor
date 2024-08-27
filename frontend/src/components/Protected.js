import React, { useState, useEffect } from 'react';
import useAuth from '../hooks/useAuth';

const ProtectedResource = () => {
    const { fetchWithAuth, loading, error: authError } = useAuth();
    const [response, setResponse] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchResource = async () => {
            try {
                const res = await fetchWithAuth('/api/v1/users/protected');
                if (res.ok) {
                    const data = await res.json();
                    setResponse(data.response);
                } else {
                    setError('Failed to fetch the protected resource');
                }
            } catch (err) {
                setError('An error occurred: ' + err.message);
            }
        };

        if (!loading) {
            fetchResource();
        }
    }, [fetchWithAuth, loading]);

    if (loading) {
        return <p>Loading...</p>; // Пока идёт инициализация
    }

    return (
        <div>
            {authError ? (
                <p>Error: {authError}</p>
            ) : error ? (
                <p>Error: {error}</p>
            ) : (
                <p>{response}</p>
            )}
        </div>
    );
};

export default ProtectedResource;
