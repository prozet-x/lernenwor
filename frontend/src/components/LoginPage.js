// components/LoginPage.js
import React, { useState, useContext } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

const LoginPage = () => {
    const [name, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();
    const { setAccessToken } = useContext(AuthContext);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/v1/auth/signin', { name, password }, { withCredentials: true });
            const { accessToken } = response.data;

            // Устанавливаем accessToken в состояние
            setAccessToken(accessToken);

            // Перенаправление на главную страницу с сообщением об успешном входе
            navigate('/', { state: { fromLogin: true } });
        } catch (error) {
            setErrorMessage('Неверное имя пользователя или пароль');
        }
    };

    return (
        <div>
            <h1>Вход</h1>
            <form onSubmit={handleLogin}>
                <div>
                    <label>Имя пользователя:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                <button type="submit">Войти</button>
            </form>
        </div>
    );
};

export default LoginPage;
