import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

// Valor inicial do contexto
const AuthContext = createContext({
  token: null,
  user: null,
  loginAction: () => {},
  logOut: () => {},
});

// Provider que gerencia autenticação
export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(null);

  // Sempre que o token mudar, configura Axios e busca o usuário
  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      axios.get('http://localhost:8080/api/users/me')
        .then(res => {
          setUser(res.data);
        })
        .catch(err => {
          console.error('Erro ao buscar usuário logado:', err);
          logOut();
        });
    } else {
      delete axios.defaults.headers.common['Authorization'];
      setUser(null);
    }
  }, [token]);

  const loginAction = (newToken) => {
    setToken(newToken);
    localStorage.setItem('token', newToken);
  };

  const logOut = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
  };

  const value = { token, user, loginAction, logOut };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook customizado para usar o contexto
export const useAuth = () => {
  return useContext(AuthContext);
};