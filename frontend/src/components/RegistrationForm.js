import React, { useState, useEffect } from 'react';

function RegistrationForm() {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        email: ''
    });
    const [isUsernameValid, setUsernameValid] = useState(true);
    const [isEmailValid, setEmailValid] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => {
            if (formData.username) {
                checkUsernameExists(formData.username);
            }
        }, 500); // Задержка в 500 мс

        return () => clearTimeout(timer);
    }, [formData.username]);

    useEffect(() => {
        const timer = setTimeout(() => {
            if (formData.email) {
                checkEmailExists(formData.email);
            }
        }, 500); // Задержка в 500 мс

        return () => clearTimeout(timer);
    }, [formData.email]);

    const checkUsernameExists = async (username) => {
        try {
            const response = await fetch(`/api/v1/check-username/${username}`);
            const { exists } = await response.json();
            setUsernameValid(!exists);
        } catch (error) {
            console.error('Ошибка при проверке логина:', error);
        }
    };

    const checkEmailExists = async (email) => {
        try {
            const response = await fetch(`/api/check-email/${email}`);
            const { exists } = await response.json();
            setEmailValid(!exists);
        } catch (error) {
            console.error('Ошибка при проверке почты:', error);
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            alert('Пароли не совпадают!');
            return;
        }
        if (!isUsernameValid || !isEmailValid) {
            alert('Логин или почта уже используются!');
            return;
        }
        console.log('Отправка данных:', formData);
        // Здесь код для отправки данных на сервер
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>
                    Логин:
                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                    {!isUsernameValid && <div style={{ color: 'red' }}>Этот логин уже занят</div>}
                </label>
            </div>
            <div>
                <label>
                    Пароль:
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>
            <div>
                <label>
                    Подтверждение пароля:
                    <input
                        type="password"
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>
            <div>
                <label>
                    Почтовый адрес:
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                    {!isEmailValid && <div style={{ color: 'red' }}>Этот email уже используется</div>}
                </label>
            </div>
            <div>
                <button type="submit">Зарегистрироваться</button>
            </div>
        </form>
    );
}

export default RegistrationForm;
