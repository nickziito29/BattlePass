import React, { useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function LoginSuccessPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { loginAction } = useAuth();
  const handled = useRef(false);

  useEffect(() => {
    if (handled.current) return;
    handled.current = true;

    const params = new URLSearchParams(location.search);
    const token = params.get('token');

    if (token) {
      loginAction(token);
      navigate('/');
    } else {
      console.error('Token não encontrado após o login OAuth2.');
      navigate('/auth', { state: { error: 'Falha ao autenticar com o Google' } });
    }
  }, [location, navigate, loginAction]);

  return (
    <div style={{ textAlign: 'center', marginTop: '2rem' }}>
      <h2>Processando seu login...</h2>
      <p>Por favor, aguarde enquanto finalizamos a autenticação.</p>
    </div>
  );
}

export default LoginSuccessPage;