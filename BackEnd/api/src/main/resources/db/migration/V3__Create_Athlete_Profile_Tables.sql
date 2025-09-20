-- Cria a tabela para os perfis específicos de atletas
CREATE TABLE athlete_profiles (
                                  id UUID PRIMARY KEY,
                                  weight_kg NUMERIC(5, 2), -- Ex: 123.45
                                  height_cm NUMERIC(5, 2), -- Ex: 200.50
                                  reach_cm NUMERIC(5, 2),
                                  team VARCHAR(255),
                                  coach VARCHAR(255),
                                  grade VARCHAR(255),
                                  category VARCHAR(255),
                                  record VARCHAR(255),
    -- Garante que um usuário só pode ter um perfil de atleta
                                  user_id UUID NOT NULL UNIQUE,
                                  CONSTRAINT fk_user_athlete_profile
                                      FOREIGN KEY(user_id)
                                          REFERENCES users(id)
                                          ON DELETE CASCADE
);

-- Cria a tabela de junção para o relacionamento Muitos-para-Muitos
-- entre atletas e modalidades
CREATE TABLE athlete_modalities (
                                    athlete_profile_id UUID NOT NULL,
                                    modality_id INTEGER NOT NULL,
                                    PRIMARY KEY (athlete_profile_id, modality_id),
                                    CONSTRAINT fk_athlete_profile
                                        FOREIGN KEY(athlete_profile_id)
                                            REFERENCES athlete_profiles(id)
                                            ON DELETE CASCADE,
                                    CONSTRAINT fk_modality
                                        FOREIGN KEY(modality_id)
                                            REFERENCES modalities(id)
                                            ON DELETE CASCADE
);