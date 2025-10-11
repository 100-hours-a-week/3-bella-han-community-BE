package com.ktbweek4.community.post.repository;


import com.ktbweek4.community.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
