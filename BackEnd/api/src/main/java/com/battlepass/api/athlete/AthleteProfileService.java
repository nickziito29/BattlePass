package com.battlepass.api.athlete;

import com.battlepass.api.modality.Modality;
import com.battlepass.api.modality.ModalityRepository;
import com.battlepass.api.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AthleteProfileService {

    private final AthleteProfileRepository profileRepository;
    private final ModalityRepository modalityRepository;

    @Transactional
    public AthleteProfile createOrUpdateProfile(User user, AthleteProfileUpdateDTO dto) {
        AthleteProfile profile = profileRepository.findByUser_Id(user.getId())
                .orElse(new AthleteProfile());

        profile.setUser(user);
        profile.setWeightKg(dto.weightKg());
        profile.setHeightCm(dto.heightCm());
        profile.setReachCm(dto.reachCm());
        profile.setTeam(dto.team());
        profile.setCoach(dto.coach());
        profile.setGrade(dto.grade());
        profile.setCategory(dto.category());
        profile.setRecord(dto.record());

        if (dto.modalityIds() != null) {
            Set<Modality> modalities = new HashSet<>(modalityRepository.findAllById(dto.modalityIds()));
            profile.setModalities(modalities);
        }

        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public AthleteProfile getProfileByUser(User user) {
        return profileRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de atleta não encontrado para o usuário."));
    }
}