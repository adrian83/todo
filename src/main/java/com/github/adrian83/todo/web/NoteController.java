package com.github.adrian83.todo.web;

import java.util.List;

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
import com.github.adrian83.todo.service.query.ListNotesQuery;
import com.github.adrian83.todo.web.request.NewNoteRequest;
import static com.github.adrian83.todo.web.util.Security.assertPrincipalNotNull;

import jakarta.validation.Valid;

@Controller
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final TagService tagService;
    private final UserRepository userRepository;

    public NoteController(NoteService noteService, TagService tagService, UserRepository userRepository) {
        this.noteService = noteService;
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @GetMapping("/notes/new")
    public String createForm(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        assertPrincipalNotNull(principal);
        model.addAttribute("newNoteRequest", new NewNoteRequest());
        model.addAttribute("userPrincipal", principal);
        model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
        return "note_form";
    }

    @GetMapping("/notes/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserPrincipal principal) {
        assertPrincipalNotNull(principal);

        Note note = noteService.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));

        model.addAttribute("newNoteRequest", new NewNoteRequest(
                note.getTitle(),
                note.getContent(),
                note.getTags().stream().map(Tag::getId).toArray(Long[]::new)
        ));
        model.addAttribute("noteId", id);
        model.addAttribute("userPrincipal", principal);
        model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
        logger.info("Editing note: {} for user: {}", note, principal.getUserId());
        return "note_form";
    }

    @PostMapping("/notes")
    public String create(@Valid @ModelAttribute("newNoteRequest") NewNoteRequest form,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal,
            Model model) {

        assertPrincipalNotNull(principal);

        List<Long> tagIdsList = form.getTagIds() != null ? List.of(form.getTagIds()) : List.of();

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
            return "note_form";
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        var createNoteCommand = new CreateNoteCommand(
                user,
                form.getTitle(),
                form.getContent(),
                tagIdsList
        );

        noteService.addNote(createNoteCommand);
        return "redirect:/notes?message=Note+saved+successfully";
    }

    @PostMapping("/notes/{id}")
    public String update(@PathVariable("id") Long id,
            @Valid @ModelAttribute("newNoteRequest") NewNoteRequest form,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {

        assertPrincipalNotNull(principal);

        List<Long> tagIdsList = form.getTagIds() != null ? List.of(form.getTagIds()) : List.of();

        logger.info("Updating note id: {} with tags: {} for user: {}", id, tagIdsList, principal.getUserId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("noteId", id);
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
            return "note_form";
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        var updateNoteCommand = new UpdateNoteCommand(
                user,
                id,
                form.getTitle(),
                form.getContent(),
                tagIdsList
        );

        noteService.updateNote(updateNoteCommand);
        return "redirect:/notes?message=Note+updated+successfully";
    }

    @GetMapping("/notes")
    public String list(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        var query = new ListNotesQuery(user);
        var notes = noteService.listNotesByUser(query);

        model.addAttribute("userPrincipal", principal);
        model.addAttribute("notes", notes);
        return "note_list";
    }

    @PostMapping("/notes/{id}/delete")
    public String delete(@PathVariable("id") Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        var command = new DeleteNoteCommand(user, id);
        noteService.deleteNote(command);
        return "redirect:/notes?message=Note+deleted+successfully";
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public String handleNotFound(NoteNotFoundException ex) {
        return "redirect:/notes?message=Note+not+found";
    }
}
