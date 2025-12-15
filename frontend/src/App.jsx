import React from "react";
import { Routes, Route, Navigate} from "react-router-dom";
import ProtectedRoute from "./auth/ProtectedRoute";
import './App.css';
import Home from './pages/HomePage/Home/Home';
import TopicsPage from './pages/TopicsPage/TopicsPage';
import LoginPage from './pages/LoginPage/LoginPage';
import LearnPage from './pages/LearnPage/LearnPage'
import Visualizer from './pages/VizualizationPage/Visualizer';
import { Toaster } from 'react-hot-toast';  


export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/visualizer" element={<Visualizer />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Protected Routes */}
        <Route path="/topics" element={<ProtectedRoute><TopicsPage /></ProtectedRoute>} />
        <Route path="/learn/:topicId" element={<ProtectedRoute><LearnPage /></ProtectedRoute>} />
        <Route path="/learn" element={<ProtectedRoute><LearnPage /></ProtectedRoute>} />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      <Toaster position="top-right" reverseOrder={false}/>
      
    </>
  );
}