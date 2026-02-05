package com.github.adrian83.todo.web;

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

import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.repository.UserRepository;
import com.github.adrian83.todo.security.UserPrincipal;
import com.github.adrian83.todo.service.TagService;
import com.github.adrian83.todo.service.exception.TagNotFoundException;
import com.github.adrian83.todo.web.request.NewTagRequest;

import jakarta.validation.Valid;


@Controller
public class TagController {

    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    private final TagService tagService;
    private final UserRepository userRepository;

    public TagController(TagService tagService, UserRepository userRepository) {
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @GetMapping("/tags/new")
    public String createForm(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        model.addAttribute("newTagRequest", new NewTagRequest());
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
        }
        return "tag_form";
    }

    @GetMapping("/tags/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserPrincipal principal) {
        Tag tag = tagService.findById(id)
            .orElseThrow(() -> new TagNotFoundException(id));
        model.addAttribute("newTagRequest", new NewTagRequest(tag.getName()));
        model.addAttribute("tagId", id);
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
        }
        return "tag_form";
    }

    @PostMapping("/tags")
    public String create(@Valid @ModelAttribute("newTagRequest") NewTagRequest form,
                         BindingResult bindingResult,
                         Model model,
                         @AuthenticationPrincipal UserPrincipal principal) {
        if (bindingResult.hasErrors()) {
            if (principal != null) {
                model.addAttribute("userPrincipal", principal);
            }
            model.addAttribute("hasErrors", true);
            return "tag_form";
        }

        User user = userRepository.findById(principal.getUserId())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        tagService.addTag(user, form.getName());
        return "redirect:/tags?message=Tag+saved+successfully";
    }

    @PostMapping("/tags/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("newTagRequest") NewTagRequest form,
                         BindingResult bindingResult,
                         Model model,
                         @AuthenticationPrincipal UserPrincipal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("tagId", id);
            if (principal != null) {
                model.addAttribute("userPrincipal", principal);
            }
            return "tag_form";
        }
        tagService.updateTag(id, form.getName());
        return "redirect:/tags?message=Tag+updated+successfully";
    }

    @GetMapping("/tags")
    public String list(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("tags", tagService.listTagsByUser(principal.getUserId()));
        }
        return "tag_list";
    }

    @PostMapping("/tags/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
        return "redirect:/tags?message=Tag+deleted+successfully";
    }

    @ExceptionHandler(TagNotFoundException.class)
    public String handleNotFound(TagNotFoundException ex) {
        return "redirect:/tags?message=Tag+not+found";
    }
}
