package com.github.adrian83.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.adrian83.todo.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserId(Long userId);

    Optional<Note> findByIdAndUserId(Long id, Long userId);

    @Query("select n from Note n where n.user.id = :userId and (lower(n.title) like lower(concat('%', :text, '%')) or lower(n.content) like lower(concat('%', :text, '%'))) order by n.createdAt desc")
    List<Note> searchByUserIdAndText(@Param("userId") Long userId, @Param("text") String text);

    @Query("select n from Note n join n.tags t where n.user.id = :userId and t.id in :tagIds group by n having count(distinct t.id) = :tagCount order by n.createdAt desc")
    List<Note> searchByUserIdAndTags(@Param("userId") Long userId, @Param("tagIds") List<Long> tagIds, @Param("tagCount") Long tagCount);

    @Query("select n from Note n join n.tags t where n.user.id = :userId and (lower(n.title) like lower(concat('%', :text, '%')) or lower(n.content) like lower(concat('%', :text, '%'))) and t.id in :tagIds group by n having count(distinct t.id) = :tagCount order by n.createdAt desc")
    List<Note> searchByUserIdAndTextAndTags(@Param("userId") Long userId, @Param("text") String text, @Param("tagIds") List<Long> tagIds, @Param("tagCount") Long tagCount);
}

