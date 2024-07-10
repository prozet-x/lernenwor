import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function RegistrationForm() {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        email: ''
    });
    const [isUsernameValid, setUsernameValid] = useState(true);
    const [isEmailValid, setEmailValid] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const timer = setTimeout(() => {
            if (formData.username) {
                checkUsernameExists(formData.username);
            }
        }, 500);
        return () => clearTimeout(timer);
    }, [formData.username]);

    useEffect(() => {
        const timer = setTimeout(() => {
            if (formData.email) {
                checkEmailExists(formData.email);
            }
        }, 500);
        return () => clearTimeout(timer);
    }, [formData.email]);

    // Existing functions: checkUsernameExists, checkEmailExists

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const checkUsernameExists = async (username) => {
        if (!username.trim()) return;

        try {
            const response = await fetch(`/api/v1/users/check-username-exists/${username}`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            setUsernameValid(!data.username_exists);
        } catch (error) {
            console.error('Ошибка при проверке логина:', error);
        }
    };

    const checkEmailExists = async (email) => {
        if (!email.trim()) return;

        try {
            const response = await fetch(`/api/v1/users/check-user-email-exists/${email}`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            setEmailValid(!data.user_email_exists);
        } catch (error) {
            console.error('Ошибка при проверке почтового адреса:', error);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            alert('Пароли не совпадают!');
            return;
        }
        if (!isUsernameValid || !isEmailValid) {
            alert('Логин или почта уже используются!');
            return;
        }

        try {
            const response = await fetch('/v1/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    name: formData.username,
                    password: formData.password,
                    passwordConfirm: formData.confirmPassword,
                    email: formData.email
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.errors || 'Failed to register.');
            }

            const userData = await response.json();
            alert('Registration successful! You can now log in with your new credentials.');
            navigate('/'); // Redirect to the home page or dashboard
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {/* Form fields */}
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            <button type="submit">Зарегистрироваться</button>
        </form>
    );
}

export default RegistrationForm;




// import React, { useState, useEffect } from 'react';
//
// function RegistrationForm() {
//     const [formData, setFormData] = useState({
//         username: '',
//         password: '',
//         confirmPassword: '',
//         email: ''
//     });
//     const [isUsernameValid, setUsernameValid] = useState(true);
//     const [isEmailValid, setEmailValid] = useState(true);
//
//     useEffect(() => {
//         const timer = setTimeout(() => {
//             if (formData.username) {
//                 checkUsernameExists(formData.username);
//             }
//         }, 500); // Задержка в 500 мс
//         return () => clearTimeout(timer);
//     }, [formData.username]);
//
//     useEffect(() => {
//         const timer = setTimeout(() => {
//             if (formData.email) {
//                 checkEmailExists(formData.email);
//             }
//         }, 500); // Задержка в 500 мс
//         return () => clearTimeout(timer);
//     }, [formData.email]);
//
//     const checkUsernameExists = async (username) => {
//         if (!username.trim()) return;
//
//         try {
//             const response = await fetch(`/api/v1/users/check-username-exists/${username}`);
//             if (!response.ok) {
//                 throw new Error('Network response was not ok');
//             }
//             const data = await response.json();
//             setUsernameValid(!data.username_exists);
//         } catch (error) {
//             console.error('Ошибка при проверке логина:', error);
//         }
//     };
//
//     const checkEmailExists = async (email) => {
//         if (!email.trim()) return;
//
//         try {
//             const response = await fetch(`/api/v1/users/check-user-email-exists/${email}`);
//             if (!response.ok) {
//                 throw new Error('Network response was not ok');
//             }
//             const data = await response.json();
//             setEmailValid(!data.user_email_exists);
//         } catch (error) {
//             console.error('Ошибка при проверке почтового адреса:', error);
//         }
//     };
//
//     const handleChange = (event) => {
//         const { name, value } = event.target;
//         setFormData(prevState => ({
//             ...prevState,
//             [name]: value
//         }));
//     };
//
//     const handleSubmit = (event) => {
//         event.preventDefault();
//         if (formData.password !== formData.confirmPassword) {
//             alert('Пароли не совпадают!');
//             return;
//         }
//         if (!isUsernameValid || !isEmailValid) {
//             alert('Логин или почта уже используются!');
//             return;
//         }
//         console.log('Отправка данных:', formData);
//         // Здесь код для отправки данных на сервер
//     };
//
//     return (
//         <form onSubmit={handleSubmit}>
//             <div>
//                 <label>
//                     Логин:
//                     <input
//                         type="text"
//                         name="username"
//                         value={formData.username}
//                         onChange={handleChange}
//                         required
//                     />
//                     {!isUsernameValid && <div style={{ color: 'red' }}>Этот логин уже занят</div>}
//                 </label>
//             </div>
//             <div>
//                 <label>
//                     Пароль:
//                     <input
//                         type="password"
//                         name="password"
//                         value={formData.password}
//                         onChange={handleChange}
//                         required
//                     />
//                 </label>
//             </div>
//             <div>
//                 <label>
//                     Подтверждение пароля:
//                     <input
//                         type="password"
//                         name="confirmPassword"
//                         value={formData.confirmPassword}
//                         onChange={handleChange}
//                         required
//                     />
//                 </label>
//             </div>
//             <div>
//                 <label>
//                     Почтовый адрес:
//                     <input
//                         type="email"
//                         name="email"
//                         value={formData.email}
//                         onChange={handleChange}
//                         required
//                     />
//                     {!isEmailValid && <div style={{ color: 'red' }}>Этот email уже используется</div>}
//                 </label>
//             </div>
//             <div>
//                 <button type="submit">Зарегистрироваться</button>
//             </div>
//         </form>
//     );
// }
//
// export default RegistrationForm;
