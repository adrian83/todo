package com.github.adrian83.todo.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewNoteRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    public NewNoteRequest() {
    }

    public NewNoteRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
