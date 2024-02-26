package com.uriel.travel.repository;

import com.uriel.travel.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagContent(String tagContent);

    @Query(value = "select tag_id, tag_content, tag_type from tag where tag_type=:tagType", nativeQuery = true)
    List<Tag> findByTagType(@Param("tagType") String tagType);
}
