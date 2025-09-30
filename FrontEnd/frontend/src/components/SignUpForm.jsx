import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext.jsx'; // <-- 1. IMPORTAR O useAuth

function SignUpForm({ onSwitchToLogin }) {
    const { loginAction } = useAuth(); // <-- 2. PEGAR A FUNÇÃO DE LOGIN DO CONTEXTO

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        nickname: '',
        email: '',
        password: '',
        birthDate: '',
        gender: '',
        pronoun: '', 
        customGender: ''
    });
    
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!formData.gender) {
            setError('Por favor, selecione um gênero.');
            return;
        }
        if (formData.gender === 'CUSTOM' && !formData.pronoun) {
            setError('Por favor, selecione seu pronome.');
            return;
        }

        setLoading(true);
        setError('');

        const dataToSend = {
            firstName: formData.firstName,
            lastName: formData.lastName,
            nickname: formData.nickname,
            email: formData.email,
            password: formData.password,
            birthDate: formData.birthDate,
            gender: formData.gender, // O backend aceita o enum, não precisa de toUpperCase()
            pronoun: formData.gender === 'CUSTOM' ? formData.pronoun : null,
            customGender: formData.gender === 'CUSTOM' ? formData.customGender : null,
        };

        try {
            // ==============================================================================
            // 3. LÓGICA DE LOGIN AUTOMÁTICO
            // ==============================================================================

            // Primeiro, tenta registrar o usuário
            await axios.post('http://localhost:8080/api/users', dataToSend);

            // Se o registro foi bem-sucedido, tenta fazer o login imediatamente
            const loginResponse = await axios.post('http://localhost:8080/api/auth/login', {
                email: formData.email,
                password: formData.password
            });

            // Se o login retornou um token, chama a loginAction do contexto
            if (loginResponse.data.token) {
                loginAction(loginResponse.data.token);
                // O AppRouter agora cuidará do redirecionamento para a página de onboarding,
                // pois o usuário será detectado como 'novo'.
            } else {
                // Caso raro em que o login falha após um registro bem-sucedido
                setError('Cadastro realizado, mas o login automático falhou. Por favor, tente logar manualmente.');
                if (onSwitchToLogin) onSwitchToLogin(e); // Volta para a tela de login
            }
            
        } catch (err) {
            if (err.response && err.response.status === 409) {
                setError('Este e-mail já está em uso. Por favor, tente outro.');
            } else {
                setError('Erro ao realizar o cadastro. Tente novamente.');
            }
            console.error('Erro no cadastro:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} noValidate>
            {/* O RESTANTE DO SEU CÓDIGO JSX CONTINUA IGUAL E ESTÁ PERFEITO */}
            <div className="form-header">
                <h2>Criar uma nova conta</h2>
                <p>É rápido e fácil.</p>
            </div>
            
            <div className="form-row">
                <input type="text" name="firstName" placeholder="Nome" value={formData.firstName} onChange={handleChange} required />
                <input type="text" name="lastName" placeholder="Sobrenome" value={formData.lastName} onChange={handleChange} required />
            </div>
            
            <input type="text" name="nickname" placeholder="Apelido (opcional)" value={formData.nickname} onChange={handleChange} />
            <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} required />
            <input type="password" name="password" placeholder="Nova senha" value={formData.password} onChange={handleChange} required />
            
            <div>
                <div className="form-row-labels">
                    <label htmlFor="birthDate">Data de nascimento</label>
                    <label htmlFor="gender">Gênero</label>
                </div>
                <div className="form-row">
                    <input id="birthDate" type="date" name="birthDate" value={formData.birthDate} onChange={handleChange} required />
                    <select id="gender" name="gender" value={formData.gender} onChange={handleChange} required>
                        <option value="" disabled>Selecione...</option>
                        <option value="FEMALE">Feminino</option>
                        <option value="MALE">Masculino</option>
                        <option value="CUSTOM">Personalizado</option>
                    </select>
                </div>
            </div>

            {formData.gender === 'CUSTOM' && (
                <div className="custom-gender-fields">
                    <select name="pronoun" value={formData.pronoun} onChange={handleChange} required>
                        <option value="" disabled>Selecione seu pronome</option>
                        <option value="ela">Feminino: "Deseje a ela um feliz aniversário!"</option>
                        <option value="ele">Masculino: "Deseje a ele um feliz aniversário!"</option>
                        <option value="elu">Neutro: "Deseje a elu um feliz aniversário!"</option>
                    </select>
                    <p className="field-description">Seu pronome fica visível para todos.</p>
                    <input 
                        type="text" 
                        name="customGender" 
                        value={formData.customGender}
                        onChange={handleChange}
                        placeholder="Gênero (opcional)" 
                    />
                </div>
            )}

            <button type="submit" className="signup-button-main" disabled={loading}>
                {loading ? 'Criando conta...' : 'Cadastre-se'}
            </button>

            {error && <p className="error-message">{error}</p>}
        </form>
    );
}

export default SignUpForm;