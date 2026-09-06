package com.github.adrian83.todo.service.query;

import com.github.adrian83.todo.domain.User;

public record FetchNoteQuery(User user, Long noteId) {
}
