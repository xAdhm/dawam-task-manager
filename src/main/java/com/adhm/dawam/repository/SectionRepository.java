package com.adhm.dawam.repository;

import com.adhm.dawam.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {

    List<Section> findByUserIdOrderByPosition(UUID userId);
}