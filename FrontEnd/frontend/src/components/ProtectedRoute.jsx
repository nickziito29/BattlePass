// src/components/ProtectedRoute.jsx
import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function ProtectedRoute() {
    const { token, user } = useAuth();

    if (!token) {
        // Se não tem token, redireciona para a página de autenticação
        return <Navigate to="/auth" replace />;
    }

    if (user && user.isNewUser) {
        // Se tem token E é um novo usuário, redireciona para a página de onboarding
        return <Navigate to="/onboarding" replace />;
    }

    // Se tem token e não é um novo usuário, renderiza a página solicitada
    return <Outlet />;
}

export default ProtectedRoute;