// src/pages/VerificationSuccessPage.jsx
import React from 'react';
import { Link } from 'react-router-dom';

function VerificationSuccessPage() {
    return (
        <div style={{ textAlign: 'center', color: 'white', padding: '2rem' }}>
            <h1>Conta Verificada com Sucesso!</h1>
            <p>Sua conta foi ativada. Agora você já pode fazer o login.</p>
            <Link to="/auth" style={{ color: '#e50914', fontSize: '1.2rem', fontWeight: 'bold' }}>
                Ir para a página de Login
            </Link>
        </div>
    );
}

export default VerificationSuccessPage;