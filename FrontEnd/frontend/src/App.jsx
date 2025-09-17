import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import AuthPage from './pages/AuthPage';
import LoginSuccessPage from './pages/LoginSuccessPage';

// Componente placeholder para a página principal (Home)
function HomePage() {
  const { token, logOut } = useAuth();
  return (
    <div>
      <h1>Bem-vindo ao BattlePass!</h1>
      <p>Seu token (início): {token?.substring(0, 30)}...</p> {/* Mostra apenas o início do token */}
      <button onClick={logOut}>Sair</button>
    </div>
  );
}

function App() {
  const { token } = useAuth();

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/auth" element={!token ? <AuthPage /> : <Navigate to="/" />} />
        <Route path="/login-success" element={<LoginSuccessPage />} />
        <Route path="/" element={token ? <HomePage /> : <Navigate to="/auth" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;