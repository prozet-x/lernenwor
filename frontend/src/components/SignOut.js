import React from 'react';
import { useHistory } from 'react-router-dom';

const Signout = () => {
    const history = useHistory();

    const handleLogout = async () => {
        try {
            // Отправляем POST-запрос на сервер для разлогинивания
            const response = await fetch('/api/v1/auth/signout', {
                method: 'POST',
                credentials: 'include', // Отправляем запрос с куки
            });

            if (response.ok) {
                localStorage.removeItem('accessToken');
                // Перенаправляем пользователя на страницу логина
                history.push('/signin');
            } else {
                console.error('Failed to log out');
            }
        } catch (error) {
            console.error('Logout error:', error);
        }
    };

    return (
        <button onClick={handleLogout}>
            Log Out
        </button>
    );
};

export default Signout;