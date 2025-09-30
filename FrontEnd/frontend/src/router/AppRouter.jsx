import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

import AuthPage from '../pages/AuthPage.jsx';
import LoginSuccessPage from '../pages/LoginSuccessPage.jsx';
import OnboardingPage from '../pages/OnboardingPage.jsx';
import ProfileEditPage from '../pages/ProfileEditPage.jsx';
import VerificationSuccessPage from '../pages/VerificationSuccessPage.jsx'; 
import EmailVerificationBanner from '../components/EmailVerificationBanner.jsx';

function HomePage() {
  const { user, logOut } = useAuth();
  const isVerified = user?.status === 'ACTIVE';

  return (
    <div>
      {/* Mostra o banner apenas se o usuário NÃO estiver verificado */}
      {!isVerified && <EmailVerificationBanner />}
      
       <div style={{ padding: '2rem', textAlign: 'center', color: 'white' }}>
        <h1>Bem-vindo ao BattlePass, {user?.firstName}!</h1>
        <p>Seu e-mail está verificado: <strong>{isVerified ? 'Sim' : 'Não'}</strong>.</p>
        <button onClick={logOut} style={{ marginTop: '1rem' }}>Sair</button>
      </div>
    </div>
  );
}

const AppRouter = () => {
  const { token, user, loading } = useAuth();

  if (loading) {
    return (
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh', color: 'white' }}>
        <h2>Carregando...</h2>
      </div>
    );
  }

  return (
    <Routes>
      {!token ? (
        <>
          <Route path="/auth" element={<AuthPage />} />
          <Route path="/login-success" element={<LoginSuccessPage />} />
          <Route path="/verification-success" element={<VerificationSuccessPage />} /> {/* <-- NOVA ROTA */}
          <Route path="*" element={<Navigate to="/auth" replace />} />
        </>
      ) : user?.isNewUser ? (
        <>
          <Route path="/onboarding" element={<OnboardingPage />} />
          <Route path="*" element={<Navigate to="/onboarding" replace />} />
        </>
      ) : (
        <>
          <Route path="/" element={<HomePage />} />
          <Route path="/profile/edit" element={<ProfileEditPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </>
      )}
    </Routes>
  );
};

export default AppRouter;