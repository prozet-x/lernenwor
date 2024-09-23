// components/DataDisplay.js
import React, { useState, useEffect, useContext, useCallback } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { useLocation, useNavigate } from 'react-router-dom';
import Modal from './Modal'; // Убедитесь, что компонент Modal правильно импортирован

function DataDisplay() {
    const [data, setData] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [modalMessage, setModalMessage] = useState('');
    const location = useLocation();
    const navigate = useNavigate();

    // Получаем axiosInstance из AuthContext
    const { axiosInstance } = useContext(AuthContext);

    useEffect(() => {
        // Функция для загрузки данных пользователей
        const fetchUsers = async () => {
            try {
                const response = await axiosInstance.get('/users');
                console.log('Данные пользователей:', response.data);
                setData(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке данных:', error);
                setModalMessage("Произошла ошибка при загрузке данных.");
                setShowModal(true);
            }
        };

        fetchUsers();

        // Проверка состояния после регистрации или входа
        if (location.state?.fromRegistration) {
            setModalMessage("Вы успешно создали учетную запись. Теперь Вы можете войти, введя свои учетные данные.");
            setShowModal(true);
            // Очистка состояния, чтобы избежать повторного отображения сообщения
            navigate(location.pathname, { replace: true, state: {} });
        } else if (location.state?.fromLogin) {
            setModalMessage("Вы успешно вошли в систему. Добро пожаловать!");
            setShowModal(true);
            // Очистка состояния, чтобы избежать повторного отображения сообщения
            navigate(location.pathname, { replace: true, state: {} });
        }
    }, [axiosInstance, location, navigate]);

    const handleCloseModal = () => {
        setShowModal(false);
    };

    // Мемоизированная функция для рендеринга таблицы
    const renderTable = useCallback((key, items) => (
        <div key={key} className="data-table-container">
            <h2>{key.charAt(0).toUpperCase() + key.slice(1)}</h2>
            <table className="data-table">
                <thead>
                <tr>
                    {Object.keys(items[0]).map(field => <th key={field}>{field}</th>)}
                </tr>
                </thead>
                <tbody>
                {items.map((item, index) => (
                    <tr key={index}>
                        {Object.values(item).map((value, i) => (
                            <td key={i}>{typeof value === 'object' ? JSON.stringify(value) : value}</td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    ), []);

    return (
        <div>
            <h1>Данные с сервера</h1>
            {Object.keys(data).map(key =>
                Array.isArray(data[key]) && data[key].length > 0 ? renderTable(key, data[key]) : null
            )}
            <Modal
                isOpen={showModal}
                onClose={handleCloseModal}
                message={modalMessage}
            />
        </div>
    );
}

export default DataDisplay;
