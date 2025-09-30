package com.battlepass.api.athlete;

import com.battlepass.api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.Set; // Adicionado para evitar ambiguidade

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AthleteProfileController {

    private final AthleteProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<AthleteProfileResponseDTO> getMyProfile(@AuthenticationPrincipal User user) {
        try {
            AthleteProfile profile = profileService.getProfileByUser(user);
            return ResponseEntity.ok(mapEntityToResponseDTO(profile));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<AthleteProfileResponseDTO> createOrUpdateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody AthleteProfileUpdateDTO dto) {
        AthleteProfile updatedProfile = profileService.createOrUpdateProfile(user, dto);
        return ResponseEntity.ok(mapEntityToResponseDTO(updatedProfile));
    }

    private AthleteProfileResponseDTO mapEntityToResponseDTO(AthleteProfile profile) {
        Set<AthleteProfileResponseDTO.ModalityDTO> modalityDTOS = profile.getModalities().stream()
                .map(modality -> new AthleteProfileResponseDTO.ModalityDTO(modality.getId(), modality.getName()))
                .collect(Collectors.toSet());

        return new AthleteProfileResponseDTO(
                profile.getId(),
                profile.getWeightKg(),
                profile.getHeightCm(),
                profile.getReachCm(),
                profile.getTeam(),
                profile.getCoach(),
                profile.getGrade(),
                profile.getCategory(),
                profile.getRecord(),
                modalityDTOS
        );
    }
}