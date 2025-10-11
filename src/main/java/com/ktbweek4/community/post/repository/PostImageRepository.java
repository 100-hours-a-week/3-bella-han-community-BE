package com.ktbweek4.community.post.repository;

import com.ktbweek4.community.post.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> { }