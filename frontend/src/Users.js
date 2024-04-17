import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Users() {
    const [user, setUser] = useState(null);

    useEffect(() => {
        // Здесь URL вашего API
        axios.get('/users')
            .then(response => {
                // Обработка полученных данных
                setUser(response.data.users[0]); // предполагаем, что нам нужен первый пользователь
            })
            .catch(error => console.error('Ошибка загрузки данных пользователя:', error));
    }, []);

    if (!user) {
        return <div>Загрузка данных...</div>;
    }

    return (
        <div>
            <h1>Данные пользователя</h1>
            <p>Имя: {user.name}</p>
            <p>Email: {user.email}</p>
            {/* Пароль обычно не отображают, но если это необходимо */}
            <p>Password: {user.password}</p>
        </div>
    );
}

export default Users;