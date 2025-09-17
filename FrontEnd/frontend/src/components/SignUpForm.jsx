import React, { useState } from 'react';
import axios from 'axios';

function SignUpForm({ onSwitchToLogin }) {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        nickname: '',
        email: '',
        password: '',
        birthDate: '',
        gender: '',
        // --- CAMPOS ADICIONADOS AO ESTADO ---
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
        
        // --- VALIDAÇÃO ATUALIZADA ---
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

        // --- LÓGICA DE ENVIO ATUALIZADA ---
        const dataToSend = {
            firstName: formData.firstName,
            lastName: formData.lastName,
            nickname: formData.nickname,
            email: formData.email,
            password: formData.password,
            birthDate: formData.birthDate,
            gender: formData.gender.toUpperCase(),
            // Envia os campos extras apenas se o gênero for 'CUSTOM'
            pronoun: formData.gender === 'CUSTOM' ? formData.pronoun : null,
            customGender: formData.gender === 'CUSTOM' ? formData.customGender : null,
        };

        try {
            await axios.post('http://localhost:8080/api/users', dataToSend);
            alert('Cadastro realizado com sucesso! Por favor, faça o login.');
            onSwitchToLogin(e);
        } catch (err) {
            setError('Erro ao realizar o cadastro. Verifique se o email já foi utilizado.');
            console.error('Erro no cadastro:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} noValidate>
            <div className="form-header">
                <h2>Criar uma nova conta</h2>
                <p>É rápido e fácil.</p>
            </div>
            
            <div className="form-row">
                <input type="text" name="firstName" placeholder="Nome" onChange={handleChange} required />
                <input type="text" name="lastName" placeholder="Sobrenome" onChange={handleChange} required />
            </div>
            
            <input type="text" name="nickname" placeholder="Apelido (opcional)" onChange={handleChange} />
            <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
            <input type="password" name="password" placeholder="Nova senha" onChange={handleChange} required />
            
            <div>
                <div className="form-row-labels">
                    <label htmlFor="birthDate">Data de nascimento</label>
                    <label htmlFor="gender">Gênero</label>
                </div>
                <div className="form-row">
                    <input id="birthDate" type="date" name="birthDate" onChange={handleChange} required />
                    <select id="gender" name="gender" value={formData.gender} onChange={handleChange} required>
                        <option value="" disabled>Selecione...</option>
                        <option value="FEMALE">Feminino</option>
                        <option value="MALE">Masculino</option>
                        <option value="CUSTOM">Personalizado</option>
                    </select>
                </div>
            </div>

            {/* --- BLOCO DE RENDERIZAÇÃO CONDICIONAL ADICIONADO --- */}
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
            {/* -------------------------------------------------- */}

            <button type="submit" className="signup-button-main" disabled={loading}>
                {loading ? 'Cadastrando...' : 'Cadastre-se'}
            </button>

            {error && <p className="error-message">{error}</p>}
        </form>
    );
}

export default SignUpForm;