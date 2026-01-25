package com.github.adrian83.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.adrian83.todo.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserId(Long userId);

    Optional<Note> findByIdAndUserId(Long id, Long userId);
}

