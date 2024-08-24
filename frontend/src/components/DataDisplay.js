import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import Modal from './Modal'; // Import or define the Modal component if not already done

function DataDisplay() {
    const [data, setData] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [modalMessage, setModalMessage] = useState('');
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('/api/v1/users')
            .then(response => {
                console.log(response.data);
                setData(response.data);
            })
            .catch(error => console.error('Ошибка при загрузке данных:', error));

        // Check for registration or login state
        if (location.state?.fromRegistration) {
            setModalMessage("Вы успешно создали учетную запись. Теперь Вы можете войти, введя свои учетные данные.");
            setShowModal(true);
            navigate(location.pathname, { replace: true, state: {} });
        } else if (location.state?.fromLogin) {
            setModalMessage("Вы успешно вошли в систему. Добро пожаловать!");
            setShowModal(true);
            navigate(location.pathname, { replace: true, state: {} });
        }
    }, [location, navigate]);

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const renderTable = (key, items) => (
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
    );

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
