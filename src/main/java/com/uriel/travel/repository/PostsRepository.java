package com.uriel.travel.repository;

import com.uriel.travel.domain.PostType;
import com.uriel.travel.domain.entity.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    List<Posts> findAllByPostType(PostType postType, Pageable pageable);
}
