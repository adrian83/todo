package com.github.adrian83.todo.service;

import com.github.adrian83.todo.domain.Note;
import com.github.adrian83.todo.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional
    public Note addNote(Note note) {
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public java.util.Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Transactional
    public Note updateNote(Long id, String title, String content) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<Note> listNotes() {
        return noteRepository.findAll();
    }

    @Transactional
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }
}
