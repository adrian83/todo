package com.github.adrian83.todo.service.query;

import java.util.List;
import java.util.Optional;

import com.github.adrian83.todo.domain.User;

public record ListNotesQuery(User user, Optional<String> mSearchPhrase, List<Long> tagIds) {
}