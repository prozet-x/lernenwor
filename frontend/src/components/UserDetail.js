import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

const UserDetail = () => {
    const { id } = useParams();
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get(`/api/v1/users/${id}`)
            .then(response => {
                setUserData(response.data.user);
                setLoading(false);
            })
            .catch(error => {
                setError(error);
                setLoading(false);
            });
    }, [id]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading user data: {error.message}</p>;

    return (
        <div>
            <h1>User Details</h1>
            {userData && Object.keys(userData).map((key) => (
                <div key={key}>
                    <strong>{key}:</strong> {userData[key]}
                </div>
            ))}
        </div>
    );
};

export default UserDetail;