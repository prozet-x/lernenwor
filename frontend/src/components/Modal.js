import React from 'react';

function Modal({ isOpen, onClose, message }) {
    if (!isOpen) return null;

    return (
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: 'rgba(0, 0, 0, 0.5)'
        }}>
            <div style={{
                padding: '20px',
                background: 'white',
                borderRadius: '5px',
                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                textAlign: 'center'  // Added to center the content inside this div
            }}>
                <p>{message}</p>
                <button onClick={onClose} style={{ margin: 'auto' }}>Понятно</button> {/* Center button within div */}
            </div>
        </div>
    );
}

export default Modal;