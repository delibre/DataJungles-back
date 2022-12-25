package com.delibre.datajungles.model.dto;

public class UserPrincipleData {
    private String username;
    private Long id;

    public UserPrincipleData(String username, Long id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
