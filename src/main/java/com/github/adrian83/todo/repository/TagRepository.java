package com.github.adrian83.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.domain.User;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByUserId(Long userId);

    List<Tag> findByUserIdAndIdIn(Long userId, List<Long> tagIds);

    List<Tag> findByUserAndIdIn(User user, List<Long> tagIds);
}
