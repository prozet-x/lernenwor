import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Modal from '../components/Modal';

function HomePage() {
    const location = useLocation();
    const [showModal, setShowModal] = useState(false);
    const [modalMessage, setModalMessage] = useState('');

    useEffect(() => {
        if (location.state?.fromRegistration) {
            setModalMessage('Registration successful! You can now log in with your new credentials.');
            setShowModal(true);
        } else if (location.state?.fromLogin) {
            setModalMessage('Login successful! Welcome back.');
            setShowModal(true);
        }
    }, [location.state]);

    const handleCloseModal = () => {
        setShowModal(false);
    };

    return (
        <div>
            <h1>Welcome to the Home Page</h1>
            <Modal
                isOpen={showModal}
                onClose={handleCloseModal}
                message={modalMessage}
            />
        </div>
    );
}

export default HomePage;