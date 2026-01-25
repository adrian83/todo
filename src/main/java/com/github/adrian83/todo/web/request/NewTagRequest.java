package com.github.adrian83.todo.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewTagRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    public NewTagRequest() {
    }

    public NewTagRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
