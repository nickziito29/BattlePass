import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './ProfileEditPage.css'; // Certifique-se de que o arquivo CSS existe

function ProfileEditPage() {
    const navigate = useNavigate();

    // Estado para armazenar os dados do formulário
    const [formData, setFormData] = useState({
        weightKg: '',
        heightCm: '',
        reachCm: '',
        team: '',
        coach: '',
        grade: '',
        category: '',
        record: '',
        modalities: [], // Armazena os IDs das modalidades selecionadas pelo usuário
    });

    // Estado para armazenar a lista de todas as modalidades disponíveis vindas da API
    const [allModalities, setAllModalities] = useState([]);
    
    // Estados para controlar a UI (carregamento, submissão, erros e sucesso)
    const [loading, setLoading] = useState(true); // Controla o carregamento inicial dos dados
    const [submitting, setSubmitting] = useState(false); // Controla o estado de "salvando..." do botão
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    // Efeito que busca os dados iniciais (perfil do usuário e lista de modalidades)
    // Usamos useCallback para garantir que a função não seja recriada a cada renderização
    const fetchData = useCallback(async () => {
        try {
            // As duas chamadas são feitas em paralelo para otimizar o tempo de carregamento
            const [profileRes, modalitiesRes] = await Promise.all([
                axios.get('http://localhost:8080/api/profiles/me'),
                axios.get('http://localhost:8080/api/modalities')
            ]);

            // Se um perfil já existe (status 200 OK), preenchemos o formulário com os dados
            if (profileRes.data) {
                const profileData = profileRes.data;
                setFormData({
                    weightKg: profileData.weightKg || '',
                    heightCm: profileData.heightCm || '',
                    reachCm: profileData.reachCm || '',
                    team: profileData.team || '',
                    coach: profileData.coach || '',
                    grade: profileData.grade || '',
                    category: profileData.category || '',
                    record: profileData.record || '',
                    modalities: profileData.modalities.map(m => m.id) // Extrai apenas os IDs das modalidades
                });
            }
            
            // Armazena a lista completa de modalidades para popular o <select>
            setAllModalities(modalitiesRes.data);

        } catch (err) {
            // Se o erro for 404, significa que o usuário ainda não tem um perfil. Isso é normal.
            // A aplicação deve continuar e apenas buscar as modalidades.
            if (err.response && err.response.status === 404) {
                console.log("Nenhum perfil encontrado. Criando um novo.");
                try {
                    const modalitiesRes = await axios.get('http://localhost:8080/api/modalities');
                    setAllModalities(modalitiesRes.data);
                // eslint-disable-next-line no-unused-vars
                } catch (modalErr) {
                     setError('Falha crítica ao carregar as modalidades.');
                }
            } else {
                // Qualquer outro erro é um problema sério
                setError('Falha ao carregar os dados. Por favor, recarregue a página.');
                console.error("Erro ao buscar dados:", err);
            }
        } finally {
            // Independentemente do resultado, o carregamento inicial termina aqui
            setLoading(false);
        }
    }, []); // O array vazio [] garante que esta função seja criada apenas uma vez

    // Executa a função fetchData uma única vez quando o componente é montado
    useEffect(() => {
        fetchData();
    }, [fetchData]);

    // Handler genérico para atualizar o estado dos inputs de texto/número
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    // Handler específico para o <select> de múltiplas modalidades
    const handleModalityChange = (e) => {
        // Converte as opções selecionadas em um array de números (IDs)
        const selectedIds = Array.from(e.target.selectedOptions, option => parseInt(option.value, 10));
        setFormData({ ...formData, modalities: selectedIds });
    };

    // Handler para a submissão do formulário
    const handleSubmit = async (e) => {
        e.preventDefault(); // Previne o recarregamento padrão da página
        setSubmitting(true);
        setError('');
        setSuccess('');

        // Monta o objeto de dados para enviar à API, conforme esperado pelo DTO do backend
        const dataToSend = {
            ...formData,
            modalityIds: formData.modalities, // O backend espera a chave 'modalityIds'
        };
        delete dataToSend.modalities; // Remove a chave 'modalities' que não é esperada pelo DTO

        try {
            // Envia os dados para o endpoint de criação/atualização
            await axios.put('http://localhost:8080/api/profiles/me', dataToSend);
            setSuccess('Perfil salvo com sucesso! Redirecionando...');
            
            // Após 2 segundos, redireciona o usuário para a página inicial
            setTimeout(() => navigate('/'), 2000);
        } catch (err) {
            setError('Ocorreu um erro ao salvar o perfil. Tente novamente.');
            console.error("Erro ao submeter o perfil:", err);
        } finally {
            setSubmitting(false); // Reabilita o botão de salvar
        }
    };

    // Se os dados iniciais ainda estão sendo carregados, exibe uma mensagem de loading
    if (loading) {
        return <div className="loading-container">Carregando perfil...</div>;
    }

    // Renderiza o formulário principal
    return (
        <div className="profile-edit-container">
            <form onSubmit={handleSubmit} className="profile-edit-form">
                <h2>Editar Perfil de Atleta</h2>
                
                <div className="form-row">
                    <div className="form-group">
                        <label htmlFor="weightKg">Peso (kg)</label>
                        <input id="weightKg" name="weightKg" type="number" step="0.1" value={formData.weightKg} onChange={handleChange} placeholder="Ex: 77.5" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="heightCm">Altura (cm)</label>
                        <input id="heightCm" name="heightCm" type="number" value={formData.heightCm} onChange={handleChange} placeholder="Ex: 180" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="reachCm">Envergadura (cm)</label>
                        <input id="reachCm" name="reachCm" type="number" value={formData.reachCm} onChange={handleChange} placeholder="Ex: 185" />
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="team">Equipe</label>
                    <input id="team" name="team" type="text" value={formData.team} onChange={handleChange} placeholder="Nome da sua equipe" />
                </div>
                <div className="form-group">
                    <label htmlFor="coach">Treinador Principal</label>
                    <input id="coach" name="coach" type="text" value={formData.coach} onChange={handleChange} placeholder="Nome do seu treinador" />
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label htmlFor="grade">Graduação</label>
                        <input id="grade" name="grade" type="text" value={formData.grade} onChange={handleChange} placeholder="Ex: Faixa Preta" />
                    </div>
                    <div className="form-group">
                        <label htmlFor="category">Categoria de Peso</label>
                        <input id="category" name="category" type="text" value={formData.category} onChange={handleChange} placeholder="Ex: Peso Médio" />
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="record">Cartel (V-D-E)</label>
                    <input id="record" name="record" type="text" value={formData.record} onChange={handleChange} placeholder="Ex: 10-2-0" />
                </div>
                
                <div className="form-group">
                    <label htmlFor="modalities">Modalidades (segure Ctrl/Cmd para selecionar várias)</label>
                    <select id="modalities" name="modalities" multiple value={formData.modalities} onChange={handleModalityChange}>
                        {allModalities.map(modality => (
                            <option key={modality.id} value={modality.id}>
                                {modality.name}
                            </option>
                        ))}
                    </select>
                </div>
                
                {/* Exibe mensagens de erro ou sucesso para o usuário */}
                {error && <p className="form-error">{error}</p>}
                {success && <p className="form-success">{success}</p>}

                <button type="submit" disabled={submitting}>
                    {submitting ? 'Salvando...' : 'Salvar Perfil'}
                </button>
            </form>
        </div>
    );
}

export default ProfileEditPage;