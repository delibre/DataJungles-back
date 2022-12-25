package com.delibre.datajungles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "serial")
    private Long id;

    private String name;

    @Column(columnDefinition="TEXT")
    private String dataset;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    @JsonIgnore
    private Folder folder;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private FileImport fileImport;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private FileModification fileModification;

    public File() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDataset() {
        return dataset;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public FileImport getFileImport() {
        return fileImport;
    }

    public void setFileImport(FileImport fileImport) {
        this.fileImport = fileImport;
    }

    public FileModification getFileModification() {
        return fileModification;
    }

    public void setFileModification(FileModification fileModification) {
        this.fileModification = fileModification;
    }
}
