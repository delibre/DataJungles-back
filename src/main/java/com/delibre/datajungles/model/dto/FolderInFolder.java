package com.delibre.datajungles.model.dto;

import com.delibre.datajungles.model.FileModification;
import com.delibre.datajungles.model.Folder;

public class FolderInFolder {

    private Folder folder;
    private FileModification fileModification;
    private String username;

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public FileModification getFileModification() {
        return fileModification;
    }

    public void setFileModification(FileModification fileModification) {
        this.fileModification = fileModification;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
