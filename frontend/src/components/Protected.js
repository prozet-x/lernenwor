import React, { useState, useEffect } from 'react';
import useAuth from '../hooks/useAuth';

const ProtectedResource = () => {
    const { fetchWithAuth } = useAuth();
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

        fetchResource();
    }, [fetchWithAuth]);

    return (
        <div>
            {error ? (
                <p>Error: {error}</p>
            ) : (
                <p>{response}</p>
            )}
        </div>
    );
};

export default ProtectedResource;