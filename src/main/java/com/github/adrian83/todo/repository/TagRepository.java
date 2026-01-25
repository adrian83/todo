package com.github.adrian83.todo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.adrian83.todo.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByUserId(Long userId);

    Set<Tag> findByUserIdAndIdIn(Long userId, Set<Long> tagIds);
}
