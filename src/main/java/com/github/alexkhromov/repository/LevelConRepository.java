package com.github.alexkhromov.repository;

import com.github.alexkhromov.model.entity.LevelCon;
import com.github.alexkhromov.model.enums.LevelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelConRepository extends JpaRepository<LevelCon, Long> {

    LevelCon findByType(LevelType type);
}