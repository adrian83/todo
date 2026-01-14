package com.github.adrian83.todo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.adrian83.todo.domain.Note;
import com.github.adrian83.todo.service.NoteNotFoundException;
import com.github.adrian83.todo.service.NoteService;

import jakarta.validation.Valid;


@Controller
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes/new")
    public String createForm(Model model) {
        model.addAttribute("noteForm", new NoteForm());
        return "note_form";
    }

    @GetMapping("/notes/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Note note = noteService.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
        model.addAttribute("noteForm", new NoteForm(note.getTitle(), note.getContent()));
        model.addAttribute("noteId", id);
        return "note_form";
    }

    @PostMapping("/notes")
    public String create(@Valid @ModelAttribute("noteForm") NoteForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "note_form";
        }

        Note note = new Note(form.getTitle(), form.getContent());
        noteService.addNote(note);
        return "redirect:/?message=Note+saved+successfully";
    }

    @PostMapping("/notes/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("noteForm") NoteForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("noteId", id);
            return "note_form";
        }
        noteService.updateNote(id, form.getTitle(), form.getContent());
        return "redirect:/notes?message=Note+updated+successfully";
    }

    @GetMapping("/notes")
    public String list(Model model) {
        model.addAttribute("notes", noteService.listNotes());
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
