package com.uriel.travel.repository;

import com.uriel.travel.domain.Tag;
import com.uriel.travel.domain.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTagContent(String tagContent);

    @Query("select t.tagContent from Tag t where t.tagType=:tagType")
    List<String> findByTagType(TagType tagType);
}
