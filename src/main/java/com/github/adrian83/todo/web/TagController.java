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
import org.springframework.web.bind.annotation.RequestParam;

import com.github.adrian83.todo.domain.Tag;
import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.repository.UserRepository;
import com.github.adrian83.todo.security.UserPrincipal;
import com.github.adrian83.todo.service.TagService;
import com.github.adrian83.todo.service.command.CreateTagCommand;
import com.github.adrian83.todo.service.command.DeleteTagCommand;
import com.github.adrian83.todo.service.command.UpdateTagCommand;
import com.github.adrian83.todo.service.exception.TagNotFoundException;
import com.github.adrian83.todo.service.query.FindTagQuery;
import com.github.adrian83.todo.service.query.ListTagsQuery;
import com.github.adrian83.todo.web.request.NewTagRequest;
import com.github.adrian83.todo.web.util.ErrorMessage;
import com.github.adrian83.todo.web.util.InfoMessage;
import com.github.adrian83.todo.web.util.ModelUtil;
import com.github.adrian83.todo.web.util.RedirectBuilder;
import static com.github.adrian83.todo.web.util.Security.assertPrincipalNotNull;

import jakarta.validation.Valid;

@Controller
public class TagController {

    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    private static final String TAG_FORM_VIEW = "tag_form";
    private static final String TAG_LIST_VIEW = "tag_list";

    private static final String TAG_LIST_PATH = "/tags";

    private final TagService tagService;
    private final UserRepository userRepository;

    public TagController(TagService tagService, UserRepository userRepository) {
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @GetMapping("/tags/new")
    public String createForm(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        assertPrincipalNotNull(principal);

        model.addAttribute("newTagRequest", new NewTagRequest());
        model.addAttribute("userPrincipal", principal);

        return TAG_FORM_VIEW;
    }

    @GetMapping("/tags/{id}/edit")
    public String editForm(
            @PathVariable("id") Long id,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        assertPrincipalNotNull(principal);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Tag tag = tagService.findById(new FindTagQuery(user, id))
                .orElseThrow(() -> new TagNotFoundException(id));

        model.addAttribute("newTagRequest", new NewTagRequest(tag.getName()));
        model.addAttribute("tagId", id);
        model.addAttribute("userPrincipal", principal);
        return TAG_FORM_VIEW;
    }

    @PostMapping("/tags")
    public String create(
            @Valid @ModelAttribute("newTagRequest") NewTagRequest form,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error
    ) {
        assertPrincipalNotNull(principal);

        if (bindingResult.hasErrors()) {
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("hasErrors", true);

            ModelUtil.enrichWithMessageAndError(model, message, error);
            return TAG_FORM_VIEW;
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        tagService.addTag(new CreateTagCommand(user, form.getName()));

        return new RedirectBuilder(TAG_LIST_PATH)
                .addInfoMessage(InfoMessage.TAG_CREATED_SUCCESSFULLY)
                .build();
    }

    @PostMapping("/tags/{id}")
    public String update(@PathVariable("id") Long id,
            @Valid @ModelAttribute("newTagRequest") NewTagRequest form,
            BindingResult bindingResult,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        assertPrincipalNotNull(principal);

        if (bindingResult.hasErrors()) {
            model.addAttribute("tagId", id);
            model.addAttribute("userPrincipal", principal);
            model.addAttribute("hasErrors", true);

            ModelUtil.enrichWithMessageAndError(model, message, error);
            return TAG_FORM_VIEW;
        }

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        tagService.updateTag(new UpdateTagCommand(user, id, form.getName()));

        return new RedirectBuilder(TAG_LIST_PATH)
                .addInfoMessage(InfoMessage.TAG_UPDATED_SUCCESSFULLY)
                .build();
    }

    @GetMapping("/tags")
    public String list(
            Model model,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error
    ) {
        assertPrincipalNotNull(principal);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("userPrincipal", principal);
        model.addAttribute("tags", tagService.listTagsByUser(new ListTagsQuery(user)));

        ModelUtil.enrichWithMessageAndError(model, message, error);

        return TAG_LIST_VIEW;
    }

    @PostMapping("/tags/{id}/delete")
    public String delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        assertPrincipalNotNull(principal);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        tagService.deleteTag(new DeleteTagCommand(user, id));

        return new RedirectBuilder(TAG_LIST_PATH)
                .addInfoMessage(InfoMessage.TAG_DELETED_SUCCESSFULLY)
                .build();
    }

    @ExceptionHandler(TagNotFoundException.class)
    public String handleNotFound(TagNotFoundException ex) {
        return new RedirectBuilder(TAG_LIST_PATH)
                .addErrorMessage(ErrorMessage.TAG_NOT_FOUND)
                .build();
    }
}
