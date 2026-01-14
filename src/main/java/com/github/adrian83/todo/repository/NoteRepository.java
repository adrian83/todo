package com.github.adrian83.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.adrian83.todo.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
