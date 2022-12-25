package com.delibre.datajungles.model.dto;

import java.util.List;

public class ObjectsInFolder {

    private List<FolderInFolder> folderInFolderList;
    private List<FileInFolder> fileInFolderList;

    public List<FolderInFolder> getFolderInFolderList() {
        return folderInFolderList;
    }

    public void setFolderInFolderList(List<FolderInFolder> folderInFolderList) {
        this.folderInFolderList = folderInFolderList;
    }

    public List<FileInFolder> getFileInFolderList() {
        return fileInFolderList;
    }

    public void setFileInFolderList(List<FileInFolder> fileInFolderList) {
        this.fileInFolderList = fileInFolderList;
    }
}
