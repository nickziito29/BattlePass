import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext.jsx';
import './OnboardingPage.css';

function OnboardingPage() {
    const navigate = useNavigate();
    const { user, refreshUser } = useAuth();
    const [loading, setLoading] = useState(false);

    const handleChoice = async (choice) => {
        setLoading(true);
        try {
            await axios.put('http://localhost:8080/api/users/me/complete-onboarding');
            await refreshUser(); // Força a atualização do estado 'user' no contexto

            if (choice === 'athlete') {
                navigate('/profile/edit');
            } else {
                navigate('/');
            }
        } catch (error) {
            console.error("Falha ao completar o onboarding:", error);
            alert("Ocorreu um erro. Por favor, tente novamente.");
            setLoading(false);
        }
    };

    return (
        <div className="onboarding-container">
            <div className="onboarding-card">
                <h1>Bem-vindo ao BattlePass, {user?.firstName}!</h1>
                <p>Para começar, nos diga como você quer usar a plataforma.</p>
                
                <div className="choice-buttons">
                    <button onClick={() => handleChoice('athlete')} disabled={loading}>
                        {loading ? 'Aguarde...' : 'Sou um Atleta / Lutador'}
                    </button>
                    <button onClick={() => handleChoice('enthusiast')} disabled={loading}>
                        {loading ? 'Aguarde...' : 'Sou um Entusiasta / Fã'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default OnboardingPage;