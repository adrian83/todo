package com.github.adrian83.todo.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.github.adrian83.todo.domain.Note;
import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.domain.User;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void whenSearchByTags_shouldReturnOnlyNotesContainingAllTags() {
        User user = userRepository.save(new User("user1", "u1@example.com", "pwd"));
        Tag t1 = tagRepository.save(new Tag(user, "t1"));
        Tag t2 = tagRepository.save(new Tag(user, "t2"));

        Note n1 = new Note(user, "alpha", "beta");
        n1.setTags(List.of(t1, t2));
        noteRepository.save(n1);

        Note n2 = new Note(user, "gamma", "delta");
        n2.setTags(List.of(t1));
        noteRepository.save(n2);

        List<Note> found = noteRepository.searchByUserIdAndTags(user.getId(), List.of(t1.getId(), t2.getId()), 2L);
        assertThat(found).containsExactly(n1);
    }

    @Test
    void whenSearchByTextAndTags_shouldApplyBothFilters() {
        User user = userRepository.save(new User("user2", "u2@example.com", "pwd"));
        Tag t1 = tagRepository.save(new Tag(user, "t1"));
        Tag t2 = tagRepository.save(new Tag(user, "t2"));

        Note n1 = new Note(user, "hello world", "some content");
        n1.setTags(List.of(t1, t2));
        noteRepository.save(n1);

        Note n2 = new Note(user, "hello there", "other");
        n2.setTags(List.of(t1));
        noteRepository.save(n2);

        List<Note> found = noteRepository.searchByUserIdAndTextAndTags(user.getId(), "hello", List.of(t1.getId(), t2.getId()), 2L);
        assertThat(found).containsExactly(n1);
    }

    @Test
    void whenSearchByTextOnly_shouldUseOldMethod() {
        User user = userRepository.save(new User("user3", "u3@example.com", "pwd"));
        Note n1 = new Note(user, "foo bar", "baz");
        noteRepository.save(n1);
        Note n2 = new Note(user, "other", "foo");
        noteRepository.save(n2);

        List<Note> found = noteRepository.searchByUserIdAndText(user.getId(), "foo");
        // searchByUserIdAndText orders by createdAt desc, so the second note
        // (n2) has a later timestamp and should appear first in the result list.
        assertThat(found).containsExactly(n2, n1);
    }
}
