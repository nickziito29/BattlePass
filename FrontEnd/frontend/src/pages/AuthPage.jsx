import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import SignUpForm from '../components/SignUpForm';
import GoogleIcon from '../assets/icons/GoogleIcon';
import { FaApple } from 'react-icons/fa';
import './AuthPage.css';

function AuthPage() {
  const [isLoginView, setIsLoginView] = useState(true);

  const switchToSignUp = (e) => {
    e.preventDefault();
    setIsLoginView(false);
  };

  const switchToLogin = (e) => {
    e.preventDefault();
    setIsLoginView(true);
  };

  const handleSocialLogin = (provider) => {
    alert(`Lógica para login com ${provider} ainda não implementada.`);
  };

  return (
    <main className="auth-container">
      <div className="auth-branding">
        <h1>BattlePass</h1>
      </div>

      <div className="auth-form-card">
        {isLoginView ? (
          <>
            <LoginForm />
            <Link to="/forgot-password" className="forgot-password-link">
              Esqueceu a senha?
            </Link>

            <div className="social-login-divider">
              <span>OU</span>
            </div>

            <div className="social-login-options">
              {/* Botão Google integrado ao backend */}
              <a
                href="http://localhost:8080/oauth2/authorization/google"
                className="social-login-button google-button"
              >
                <GoogleIcon />
                <span>Entrar com Google</span>
              </a>

              <button
                type="button"
                className="social-login-button apple-button"
                onClick={() => handleSocialLogin('Apple')}
              >
                <FaApple />
                <span>Entrar com Apple</span>
              </button>
            </div>

            <hr />
            <button onClick={switchToSignUp} className="signup-button">
              Criar nova conta
            </button>
          </>
        ) : (
          <>
            <SignUpForm onSwitchToLogin={switchToLogin} />
            <a href="/" onClick={switchToLogin} className="switch-link">
              Já tem uma conta?
            </a>
          </>
        )}
      </div>
    </main>
  );
}

export default AuthPage;