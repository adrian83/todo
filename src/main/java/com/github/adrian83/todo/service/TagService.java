package com.github.adrian83.todo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.repository.TagRepository;
import com.github.adrian83.todo.service.command.CreateTagCommand;
import com.github.adrian83.todo.service.command.DeleteTagCommand;
import com.github.adrian83.todo.service.command.UpdateTagCommand;
import com.github.adrian83.todo.service.exception.TagNotFoundException;
import com.github.adrian83.todo.service.query.FindTagQuery;
import com.github.adrian83.todo.service.query.ListTagsByIdsQuery;
import com.github.adrian83.todo.service.query.ListTagsQuery;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Tag addTag(CreateTagCommand cmd) {
        Tag tag = new Tag(cmd.getUser(), cmd.getName());
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public java.util.Optional<Tag> findById(FindTagQuery query) {
        return tagRepository.findByIdAndUser(query.getId(), query.getUser());
    }

    @Transactional
    public Tag updateTag(UpdateTagCommand cmd) {
        Tag tag = tagRepository.findByIdAndUser(cmd.getId(), cmd.getUser())
            .orElseThrow(() -> new TagNotFoundException(cmd.getId()));
        tag.setName(cmd.getName());
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public List<Tag> listTagsByUser(ListTagsQuery query) {
        return tagRepository.findByUserId(query.getUser().getId());
    }

    @Transactional
    public void deleteTag(DeleteTagCommand cmd) {
        if (!tagRepository.existsByIdAndUser(cmd.getId(), cmd.getUser())) {
            throw new TagNotFoundException(cmd.getId());
        }
        tagRepository.deleteById(cmd.getId());
    }

    @Transactional(readOnly = true)
    public List<Tag> listTagsByUserAndIds(ListTagsByIdsQuery query) {
        logger.debug("Listing tags for user: {} and ids: {}", query.getUser().getId(), query.getIds());
        return tagRepository.findByUserAndIdIn(query.getUser(), query.getIds());
    }
}
