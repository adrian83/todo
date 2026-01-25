package com.github.adrian83.todo.service.exception;

public class NoteNotFoundException extends RuntimeException {

    private final Long noteId;

    public NoteNotFoundException(Long noteId) {
        super("Note not found: " + noteId);
        this.noteId = noteId;
    }

    public Long getNoteId() {
        return noteId;
    }
}
