import React, { createContext, useState, useContext, useEffect, useCallback } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const logOut = useCallback(() => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];
  }, []);

  const fetchUser = useCallback(async (currentToken) => {
    if (currentToken) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
      try {
        const res = await axios.get('http://localhost:8080/api/users/me');
        setUser(res.data);
      } catch (err) {
        console.error('Erro ao buscar usuário (token pode ser inválido):', err);
        logOut();
      }
    } else {
      setUser(null);
    }
    setLoading(false);
  }, [logOut]);

  useEffect(() => {
    setLoading(true);
    fetchUser(token);
  }, [token, fetchUser]);

  const loginAction = (newToken) => {
    setToken(newToken);
    localStorage.setItem('token', newToken);
  };

  const refreshUser = async () => {
    if (token) {
      setLoading(true);
      await fetchUser(token);
    }
  };

  const value = { token, user, loading, loginAction, logOut, refreshUser };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  return useContext(AuthContext);
};