import React, { useState, useEffect } from 'react';

const ProtectedResource = () => {
    const [response, setResponse] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProtectedResource = async () => {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                setError('Access token not found');
                return;
            }

            try {
                const res = await fetch('/api/v1/users/protected', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });

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

        fetchProtectedResource();
    }, []);

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