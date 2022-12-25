package com.delibre.datajungles.model.dto;

import com.delibre.datajungles.model.File;
import com.delibre.datajungles.model.FileModification;

public class FileInFolder {

    private File file;
    private FileModification fileModification;
    private String username;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
