package com.github.adrian83.todo.web.util;


public class RedirectBuilder {

    private String path;
    private String queryParams = "";

    public RedirectBuilder(String path) {
        this.path = path;
    }

    public RedirectBuilder addQueryParam(String key, String value) {
        if (queryParams.isEmpty()) {
            queryParams += "?";
        } else {
            queryParams += "&";
        }
        queryParams += key + "=" + value;
        return this;
    }

    public RedirectBuilder addInfoMessage(InfoMessage message) {
        return addQueryParam("message", message.name().toLowerCase());
    }
    
    public RedirectBuilder addErrorMessage(ErrorMessage message) {
        return addQueryParam("error", message.name().toLowerCase());
    }

    public String build() {
        return "redirect:" + path + queryParams;
    }
}