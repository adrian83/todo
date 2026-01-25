package com.github.adrian83.todo.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.repository.TagRepository;
import com.github.adrian83.todo.service.exception.TagNotFoundException;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Tag addTag(User user, String name) {
        Tag tag = new Tag(user, name);
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public java.util.Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Transactional
    public Tag updateTag(Long id, String name) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new TagNotFoundException(id));
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public List<Tag> listTagsByUser(Long userId) {
        return tagRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }
        tagRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Set<Tag> listTagsByUserAndIds(Long userId, Set<Long> ids) {
        return tagRepository.findByUserIdAndIdIn(userId, ids); 
    }
}
