import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Modal from '../components/Modal';

function HomePage() {
    const location = useLocation();
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        if (location.state?.fromRegistration) {
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
                message="Registration successful! You can now log in with your new credentials."
            />
        </div>
    );
}

export default HomePage;