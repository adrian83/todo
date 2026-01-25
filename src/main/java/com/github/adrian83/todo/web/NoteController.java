package com.github.adrian83.todo.web;

import java.util.HashSet;
import java.util.Set;

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
import com.github.adrian83.todo.service.command.UpdateNoteCommand;
import com.github.adrian83.todo.service.exception.NoteNotFoundException;
import com.github.adrian83.todo.web.request.NewNoteRequest;

import jakarta.validation.Valid;


@Controller
public class NoteController {

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
        model.addAttribute("newNoteRequest", new NewNoteRequest());
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
        }
        return "note_form";
    }

    @GetMapping("/notes/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserPrincipal principal) {
        Note note = noteService.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
        model.addAttribute("newNoteRequest", new NewNoteRequest(note.getTitle(), note.getContent()));
        model.addAttribute("noteId", id);
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
            model.addAttribute("selectedTagIds", note.getTags().stream().map(Tag::getId).toList());
        }
        return "note_form";
    }

    @PostMapping("/notes")
    public String create(@Valid @ModelAttribute("newNoteRequest") NewNoteRequest form,
                         BindingResult bindingResult,
                         @RequestParam(name = "tagIds", required = false) Long[] tagIds,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        if (bindingResult.hasErrors()) {
            if (principal != null) {
                model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
            }
            return "note_form";
        }

        User user = userRepository.findById(principal.getUserId())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        var createNoteCommand = new CreateNoteCommand(
            user,
            form.getTitle(),
            form.getContent(),
            tagIds != null ? Set.of(tagIds) : Set.of()
        );  


        noteService.addNote(createNoteCommand);
        return "redirect:/notes?message=Note+saved+successfully";
    }

    @PostMapping("/notes/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("newNoteRequest") NewNoteRequest form,
                         BindingResult bindingResult,
                         @RequestParam(name = "tagIds", required = false) Long[] tagIds,
                         Model model,
                         @AuthenticationPrincipal UserPrincipal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("noteId", id);
            if (principal != null) {
                model.addAttribute("userPrincipal", principal);
                model.addAttribute("availableTags", tagService.listTagsByUser(principal.getUserId()));
            }
            return "note_form";
        }

        User user = userRepository.findById(principal.getUserId())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        var updateNoteCommand = new UpdateNoteCommand(
            user,
            id,
            form.getTitle(),
            form.getContent(),
            tagIds != null ? Set.of(tagIds) : Set.of()
        );


        noteService.updateNote(updateNoteCommand);
        return "redirect:/notes?message=Note+updated+successfully";
    }

    @GetMapping("/notes")
    public String list(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("notes", noteService.listNotesByUser(principal.getUserId()));
        }
        return "note_list";
    }

    @PostMapping("/notes/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        noteService.deleteNote(id);
        return "redirect:/notes?message=Note+deleted+successfully";
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public String handleNotFound(NoteNotFoundException ex) {
        return "redirect:/notes?message=Note+not+found";
    }
}


