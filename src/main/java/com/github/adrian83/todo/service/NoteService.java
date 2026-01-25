package com.github.adrian83.todo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.adrian83.todo.domain.Note;
import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.repository.NoteRepository;
import com.github.adrian83.todo.service.command.CreateNoteCommand;
import com.github.adrian83.todo.service.command.UpdateNoteCommand;
import com.github.adrian83.todo.service.exception.NoteNotFoundException;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final TagService tagService;

    public NoteService(NoteRepository noteRepository, TagService tagService) {
        this.noteRepository = noteRepository;
        this.tagService = tagService;
    }

    @Transactional
    public Note addNote(CreateNoteCommand createNoteCommand) {
        Note note = new Note(createNoteCommand.user(), createNoteCommand.title(), createNoteCommand.content());
        Set<Tag> tags = tagService.listTagsByUserAndIds(createNoteCommand.user().getId(),
            createNoteCommand.tagIds());
        
        note.setTags(tags);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    

    @Transactional
    public Note updateNote(UpdateNoteCommand updateNoteCommand) {
        Note note = noteRepository.findByIdAndUserId(updateNoteCommand.id(), updateNoteCommand.user().getId())
            .orElseThrow(() -> new NoteNotFoundException(updateNoteCommand.id()));
        note.setTitle(updateNoteCommand.title());
        note.setContent(updateNoteCommand.content());
        note.setTags(tagService.listTagsByUserAndIds(updateNoteCommand.user().getId(), updateNoteCommand.tagIds()));
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<Note> listNotesByUser(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }
}
