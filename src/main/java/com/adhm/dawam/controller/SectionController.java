package com.adhm.dawam.controller;

import com.adhm.dawam.dto.SectionRequest;
import com.adhm.dawam.dto.SectionResponse;
import com.adhm.dawam.entity.Section;
import com.adhm.dawam.repository.SectionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionRepository sectionRepository;

    @GetMapping
    public List<SectionResponse> listSections(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return sectionRepository.findByUserIdOrderByPosition(userId)
                .stream()
                .map(SectionResponse::from)
                .toList();
    }

    @PostMapping
    public SectionResponse createSection(@AuthenticationPrincipal Jwt jwt,
                                         @Valid @RequestBody SectionRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());

        int nextPosition = sectionRepository.findByUserIdOrderByPosition(userId).size();

        Section section = new Section();
        section.setUserId(userId);
        section.setName(request.getName());
        section.setPosition(nextPosition);

        Section saved = sectionRepository.save(section);
        return SectionResponse.from(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionResponse> renameSection(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable UUID id,
                                                         @Valid @RequestBody SectionRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return sectionRepository.findById(id)
                .filter(section -> section.getUserId().equals(userId))
                .map(section -> {
                    section.setName(request.getName());
                    Section saved = sectionRepository.save(section);
                    return ResponseEntity.ok(SectionResponse.from(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@AuthenticationPrincipal Jwt jwt,
                                              @PathVariable UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return sectionRepository.findById(id)
                .filter(section -> section.getUserId().equals(userId))
                .map(section -> {
                    sectionRepository.delete(section);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reorder")
    public List<SectionResponse> reorderSections(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestBody List<UUID> orderedIds) {
        UUID userId = UUID.fromString(jwt.getSubject());

        List<Section> userSections = sectionRepository.findByUserIdOrderByPosition(userId);

        Map<UUID, Section> sectionsById = userSections.stream()
                .collect(Collectors.toMap(Section::getId, s -> s));

        List<Section> updated = new java.util.ArrayList<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            Section section = sectionsById.get(orderedIds.get(i));
            if (section != null) {
                section.setPosition(i);
                updated.add(section);
            }
        }

        sectionRepository.saveAll(updated);

        return sectionRepository.findByUserIdOrderByPosition(userId)
                .stream()
                .map(SectionResponse::from)
                .toList();
    }
}