import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.jsx';
import { AuthProvider } from './context/AuthContext'; // <-- 1. IMPORTAR O PROVIDER

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider> {/* <-- 2. ENVOLVER O APP COM O PROVIDER */}
      <App />
    </AuthProvider>
  </React.StrictMode>,
);