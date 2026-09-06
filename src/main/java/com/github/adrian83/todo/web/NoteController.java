package com.github.adrian83.todo.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.adrian83.todo.domain.Note;
import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.repository.UserRepository;
import com.github.adrian83.todo.security.UserPrincipal;
import com.github.adrian83.todo.service.NoteService;
import com.github.adrian83.todo.service.TagService;
import com.github.adrian83.todo.service.command.CreateNoteCommand;
import com.github.adrian83.todo.service.command.DeleteNoteCommand;
import com.github.adrian83.todo.service.command.UpdateNoteCommand;
import com.github.adrian83.todo.service.exception.NoteNotFoundException;
import com.github.adrian83.todo.service.query.FetchNoteQuery;
import com.github.adrian83.todo.service.query.ListNotesQuery;
import com.github.adrian83.todo.service.query.ListTagsQuery;
import com.github.adrian83.todo.web.request.NewNoteRequest;
import com.github.adrian83.todo.web.util.ErrorMessage;
import com.github.adrian83.todo.web.util.InfoMessage;
import com.github.adrian83.todo.web.util.ModelUtil;
import com.github.adrian83.todo.web.util.RedirectBuilder;
import static com.github.adrian83.todo.web.util.Security.assertPrincipalNotNull;

import jakarta.validation.Valid;

@Controller
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private static final String NOTE_FORM_VIEW = "note_form";
    private static final String NOTE_LIST_VIEW = "note_list";

    private static final String NOTE_LIST_PATH = "/notes";

    private final NoteService noteService;
    private final TagService tagService;
    private final UserRepository userRepository;

    public NoteController(NoteService noteService, TagService tagService, UserRepository userRepository) {
        this.noteService = noteService;
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @GetMapping("/notes/new")
    public String createForm(
        Model model, 
        @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error
        ) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        ModelUtil.enrichWithMessageAndError(model, message, error);

        model.addAttribute("newNoteRequest", new NewNoteRequest());
        model.addAttribute("userPrincipal", principal);
        model.addAttribute("availableTags", tagService.listTagsByUser(new ListTagsQuery(user)));
        return NOTE_FORM_VIEW;
    }

    @GetMapping("/notes/{id}/edit")
    public String editForm(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Note note = noteService.findById(new FetchNoteQuery(user, id))
                .orElseThrow(() -> new NoteNotFoundException(id));

        var newNoteRequest = new NewNoteRequest(
                note.getTitle(),
                note.getContent(),
                note.getTags().stream().map(Tag::getId).toArray(Long[]::new)
        );

        ModelUtil.enrichWithMessageAndError(model, message, error);

        model.addAttribute("newNoteRequest", newNoteRequest);
        model.addAttribute("noteId", id);
        model.addAttribute("userPrincipal", principal);
        model.addAttribute("availableTags", tagService.listTagsByUser(new ListTagsQuery(user)));

        logger.info("Editing note: {} for user: {}", note, principal.getUserId());
        return NOTE_FORM_VIEW;
    }

    @PostMapping(NOTE_LIST_PATH)
    public String create(
            @Valid @ModelAttribute("newNoteRequest") NewNoteRequest form,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal,
            Model model) {

        assertPrincipalNotNull(principal);

            User user = userRepository.findById(principal.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("availableTags", tagService.listTagsByUser(new ListTagsQuery(user)));
            return NOTE_FORM_VIEW;
        }

        var createNoteCommand = new CreateNoteCommand(
                user,
                form.getTitle(),
                form.getContent(),
                form.getTagIds() != null ? List.of(form.getTagIds()) : List.of()
        );

        noteService.addNote(createNoteCommand);

        return new RedirectBuilder(NOTE_LIST_PATH)
                .addInfoMessage(InfoMessage.NOTE_CREATED_SUCCESSFULLY)
                .build();
    }

    @PostMapping("/notes/{id}")
    public String update(@PathVariable("id") Long id,
            @Valid @ModelAttribute("newNoteRequest") NewNoteRequest form,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {

        assertPrincipalNotNull(principal);

        logger.info("Updating note id: {} with tags: {} for user: {}", id, form.getTagIds(), principal.getUserId());

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("noteId", id);
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("availableTags", tagService.listTagsByUser(new ListTagsQuery(user)));
            return NOTE_FORM_VIEW;
        }

        var updateNoteCommand = new UpdateNoteCommand(
                user,
                id,
                form.getTitle(),
                form.getContent(),
                form.getTagIds() != null ? List.of(form.getTagIds()) : List.of()
        );

        noteService.updateNote(updateNoteCommand);

        return new RedirectBuilder(NOTE_LIST_PATH)
                .addInfoMessage(InfoMessage.NOTE_UPDATED_SUCCESSFULLY)
                .build();
    }

    @GetMapping(NOTE_LIST_PATH)
    public String list(
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "error", required = false) String error,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<Long> tagIdsForQuery = tagIds == null ? List.of() : tagIds;

        var query = new ListNotesQuery(user, Optional.ofNullable(q), tagIdsForQuery);
        List<Note> notes = noteService.listNotesByUser(query);

        model.addAttribute("userPrincipal", principal);
        model.addAttribute("notes", notes);
        model.addAttribute("q", q);
        model.addAttribute("selectedTagIds", tagIds);
        model.addAttribute("availableTags", tagService.listTagsByUser(new ListTagsQuery(user)));
        
        ModelUtil.enrichWithMessageAndError(model, message, error);
        
        return NOTE_LIST_VIEW;
    }

    @PostMapping("/notes/{id}/delete")
    public String delete(@PathVariable("id") Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        var command = new DeleteNoteCommand(user, id);
        noteService.deleteNote(command);

        return new RedirectBuilder(NOTE_LIST_PATH)
                .addInfoMessage(InfoMessage.NOTE_DELETED_SUCCESSFULLY)
                .build();
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public String handleNotFound(NoteNotFoundException ex) {
        return new RedirectBuilder(NOTE_LIST_PATH)
                .addErrorMessage(ErrorMessage.NOTE_NOT_FOUND)
                .build();
    }
}
