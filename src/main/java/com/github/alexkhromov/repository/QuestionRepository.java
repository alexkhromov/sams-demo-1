package com.github.alexkhromov.repository;

import com.github.alexkhromov.model.dto.ReadAllQuestionDTO;
import com.github.alexkhromov.model.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT new com.github.alexkhromov.model.dto.ReadAllQuestionDTO( " +
            "q.id AS id, t.title AS title, q.link AS link, " +
            "ll.levelLocalized AS level, q.isFullyLocalized AS isFullyLocalized, " +
            "CASE WHEN q.user.id = :userId THEN TRUE ELSE FALSE END) " +
            "FROM Question q JOIN q.titles t JOIN q.level.localizedLevels ll " +
            "WHERE t.locale.code = :locale AND ll.locale.code = :locale " +
            "AND q.level.type LIKE COALESCE(:level, '%') " +
            "AND (q.user.id = :userId OR q.user.id <> :userId) ")
    Page<ReadAllQuestionDTO> findAll(@Param("level") String level,
                                     @Param("locale") String locale,
                                     @Param("userId") Long userId,
                                     Pageable pageable);

    @Query(value = "SELECT new com.github.alexkhromov.model.dto.ReadAllQuestionDTO( " +
            "q.id AS id, t.title AS title, q.link AS link, ll.levelLocalized AS level, " +
            "q.isFullyLocalized AS isFullyLocalized, CAST(NULL AS boolean)) " +
            "FROM Question q JOIN q.titles t JOIN q.level.localizedLevels ll " +
            "WHERE t.locale.code = :locale AND ll.locale.code = :locale " +
            "AND q.isFullyLocalized IS FALSE")
    Page<ReadAllQuestionDTO> findAllForTranslation(@Param("locale") String locale,
                                                   Pageable pageable);

    @Query(value = "SELECT new com.github.alexkhromov.model.dto.ReadAllQuestionDTO( " +
            "q.id AS id, t.title AS title, q.link AS link, " +
            "ll.levelLocalized AS level, q.isFullyLocalized AS isFullyLocalized, " +
            "CASE WHEN q.user.id = :userId THEN TRUE ELSE FALSE END) " +
            "FROM Question q JOIN q.titles t JOIN q.level.localizedLevels ll " +
            "WHERE t.locale.code = :locale AND ll.locale.code = :locale " +
            "AND t.title LIKE COALESCE(CONCAT('%', :query, '%'), '%') " +
            "AND (q.user.id = :userId OR q.user.id <> :userId) ")
    Page<ReadAllQuestionDTO> findByQuery(@Param("query") String query,
                                     @Param("locale") String locale,
                                     @Param("userId") Long userId,
                                     Pageable pageable);

}