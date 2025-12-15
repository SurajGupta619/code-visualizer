import React from 'react';

const Modal = ({ isOpen, onClose, children }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
            <div
                className="fixed inset-0 bg-black bg-opacity-50 backdrop-filter backdrop-blur-sm"
                onClick={onClose}
            ></div>
            {children}
        </div>
    );
};

export default Modal;