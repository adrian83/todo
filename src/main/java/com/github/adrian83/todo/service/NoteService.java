package com.github.adrian83.todo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.adrian83.todo.domain.Note;
import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.repository.NoteRepository;
import com.github.adrian83.todo.service.command.CreateNoteCommand;
import com.github.adrian83.todo.service.command.DeleteNoteCommand;
import com.github.adrian83.todo.service.command.UpdateNoteCommand;
import com.github.adrian83.todo.service.exception.NoteNotFoundException;
import com.github.adrian83.todo.service.query.FetchNoteQuery;
import com.github.adrian83.todo.service.query.ListNotesQuery;
import com.github.adrian83.todo.service.query.ListTagsByIdsQuery;

@Service
public class NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepository noteRepository;
    private final TagService tagService;

    public NoteService(NoteRepository noteRepository, TagService tagService) {
        this.noteRepository = noteRepository;
        this.tagService = tagService;
    }

    @Transactional
    public Note addNote(CreateNoteCommand createNoteCommand) {
        logger.debug("Creating new note with title: {}", createNoteCommand.title());
        Note note = new Note(createNoteCommand.user(), createNoteCommand.title(), createNoteCommand.content());
        List<Tag> tags = tagService.listTagsByUserAndIds(new ListTagsByIdsQuery(createNoteCommand.user(), createNoteCommand.tagIds()));

        logger.info("Associating tags: {} to the new note", tags);
        
        note.setTags(tags);
        Note savedNote = noteRepository.save(note);
        logger.info("Note created successfully: {} for user: {}", savedNote, createNoteCommand.user().getId());
        return savedNote;
    }

    @Transactional(readOnly = true)
    public Optional<Note> findById(FetchNoteQuery fetchNoteQuery) {
        return noteRepository.findByIdAndUserId(fetchNoteQuery.noteId(), fetchNoteQuery.user().getId());
    }

    @Transactional
    public Note updateNote(UpdateNoteCommand updateNoteCommand) {
        logger.debug("Updating note with id: {} for user: {}", updateNoteCommand.id(), updateNoteCommand.user().getId());
        Note note = noteRepository.findByIdAndUserId(updateNoteCommand.id(), updateNoteCommand.user().getId())
            .orElseThrow(() -> new NoteNotFoundException(updateNoteCommand.id()));
        note.setTitle(updateNoteCommand.title());
        note.setContent(updateNoteCommand.content());
        note.setTags(tagService.listTagsByUserAndIds(new ListTagsByIdsQuery(updateNoteCommand.user(), updateNoteCommand.tagIds())));
        Note updatedNote = noteRepository.save(note);
        logger.info("Note updated successfully with id: {}", updatedNote.getId());
        return updatedNote;
    }

    @Transactional(readOnly = true)
    public List<Note> listNotesByUser(ListNotesQuery listNotesQuery) {
        Long userId = listNotesQuery.user().getId();
        List<Long> tagIds = listNotesQuery.tagIds() == null ? List.of() : listNotesQuery.tagIds();
        boolean hasTags = !tagIds.isEmpty();

        return listNotesQuery.mSearchPhrase()
            .filter(s -> !s.isBlank())
            .map(phrase -> {
                if (hasTags) {
                    return noteRepository.searchByUserIdAndTextAndTags(userId, phrase, tagIds, (long) tagIds.size());
                } else {
                    return noteRepository.searchByUserIdAndText(userId, phrase);
                }
            })
            .orElseGet(() -> {
                if (hasTags) {
                    return noteRepository.searchByUserIdAndTags(userId, tagIds, (long) tagIds.size());
                } else {
                    return noteRepository.findByUserId(userId);
                }
            });
    }

    @Transactional
    public void deleteNote(DeleteNoteCommand deleteNoteCommand) {
        Long id = deleteNoteCommand.noteId();
        Long userId = deleteNoteCommand.user().getId();
        logger.debug("Deleting note with id: {} for user: {}", id, userId);
        
        Note note = noteRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new NoteNotFoundException(id));
        
        noteRepository.deleteById(id);
        logger.info("Note deleted successfully with id: {} for user: {}", id, userId);
    }
}
