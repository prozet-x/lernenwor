import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './index.css';

function DataDisplay() {
    const [data, setData] = useState({});

    useEffect(() => {
        // Здесь URL вашего API
        axios.get('http://192.168.0.105:8080/api/users')
            .then(response => {
                console.log(response.data);
                setData(response.data);
            })
            .catch(error => console.error('Ошибка при загрузке данных:', error));
    }, []);

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
            <p>{process.env.REACT_APP_BACKEND_URL}</p>
            {Object.keys(data).map(key =>
                Array.isArray(data[key]) && data[key].length > 0 ? renderTable(key, data[key]) : null
            )}
        </div>
    );
}

export default DataDisplay;