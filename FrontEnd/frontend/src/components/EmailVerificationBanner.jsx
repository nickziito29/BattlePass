import React, { useState } from 'react';
import'axios';

/**
 * Um banner que aparece no topo da página para usuários cujo e-mail ainda não foi verificado.
 * Oferece um botão para reenviar o e-mail de verificação.
 */
function EmailVerificationBanner() {
    // Estado para controlar a mensagem de feedback para o usuário (ex: "E-mail enviado!")
    const [message, setMessage] = useState('');
    // Estado para desabilitar o botão enquanto uma requisição está em andamento
    const [loading, setLoading] = useState(false);

    /**
     * Função chamada quando o usuário clica no botão "Reenviar e-mail".
     */
    const handleResend = async () => {
        setLoading(true);
        setMessage(''); // Limpa mensagens antigas

        try {
            // ==============================================================================
            // TODO: Esta funcionalidade requer um novo endpoint no back-end.
            // Por enquanto, vamos apenas simular uma resposta para o usuário.
            // Quando o endpoint `POST /api/auth/resend-verification` for criado,
            // a linha abaixo deverá ser descomentada.
            // ==============================================================================
            
            // await axios.post('http://localhost:8080/api/auth/resend-verification');
            
            // Simulação de sucesso:
            setTimeout(() => {
                setMessage('Funcionalidade de reenvio ainda em construção.');
                setLoading(false);
            }, 1000); // Simula um segundo de espera

        } catch (error) {
            setMessage('Falha ao tentar reenviar o e-mail. Tente novamente mais tarde.');
            console.error("Erro ao reenviar e-mail de verificação:", error);
            setLoading(false);
        }
    };

    return (
        <div style={{ 
            backgroundColor: '#ffc107', // Cor de alerta (amarelo/laranja)
            color: 'black', 
            padding: '1rem', 
            textAlign: 'center', 
            width: '100%', 
            boxSizing: 'border-box' 
        }}>
            <span>Sua conta não está verificada. Por favor, verifique a caixa de entrada do seu e-mail.</span>
            
            <button 
                onClick={handleResend} 
                disabled={loading} 
                style={{ marginLeft: '1rem', cursor: 'pointer', padding: '0.5rem 1rem', border: '1px solid black', borderRadius: '4px' }}
            >
                {loading ? 'Enviando...' : 'Reenviar e-mail'}
            </button>

            {/* Exibe a mensagem de feedback (sucesso ou erro) se ela existir */}
            {message && <p style={{ marginTop: '0.5rem', fontWeight: 'bold' }}>{message}</p>}
        </div>
    );
}

export default EmailVerificationBanner;