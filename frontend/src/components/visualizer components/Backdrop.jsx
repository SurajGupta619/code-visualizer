import React, { useState } from 'react'
import Modal from './Modal';

const Backdrop = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);
    return (
        <div>
            <div className="min-h-screen bg-gray-100 p-8">
                <h1 className="text-3xl font-bold mb-4">Main Content</h1>
                <p className="mb-4">This is some content behind the modal.</p>
                <button onClick={openModal} className="bg-blue-500 text-white px-4 py-2 rounded">
                    Open Modal
                </button>

                <Modal isOpen={isModalOpen} onClose={closeModal}>
                    <h2 className="text-xl font-semibold mb-2">Modal Title</h2>
                    <p>This is the content of the modal.</p>
                </Modal>
            </div>
        </div>
    )
}

export default Backdrop

