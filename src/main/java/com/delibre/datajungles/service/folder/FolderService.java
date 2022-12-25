package com.delibre.datajungles.service.folder;

import com.delibre.datajungles.model.*;
import com.delibre.datajungles.model.dto.FileInFolder;
import com.delibre.datajungles.model.dto.FolderInFolder;
import com.delibre.datajungles.model.dto.NewFolder;
import com.delibre.datajungles.model.dto.ObjectsInFolder;
import com.delibre.datajungles.model.user.Group;
import com.delibre.datajungles.model.user.User;
import com.delibre.datajungles.repository.*;
import com.delibre.datajungles.service.user.PrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private PrincipleService principleService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    public FolderRepository getFolderRepository() {
        return folderRepository;
    }

    public Folder addFolder(Long id, NewFolder newFolder) {
        Folder rootFolder = folderRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        Folder folder = new Folder();
        folder.setName(newFolder.getName());
        folder.setRootFolder(rootFolder);
        if (rootFolder.getFolders() == null) {
            rootFolder.setFolders(new HashSet<>());
        }
        rootFolder.getFolders().add(folder);
        folder = folderRepository.saveAndFlush(folder);

        if (newFolder.getIsPrivate()) {
            Group group = new Group("private_" + folder.getId() + "_" + principleService.getPrincipalUser().getId());
            group = groupRepository.saveAndFlush(group);
            User user = principleService.getPrincipalUser();

            if (user.getGroups() == null) {
                user.setGroups(new HashSet<>());
            }
            user.getGroups().add(group);
            userRepository.save(user);

            Permission permission = new Permission();
            permission.setFolder(folder);
            permission.setGroup(group);
            permission.setRights(Rights.WRITE);
            permissionRepository.save(permission);
        } else {
            if (groupRepository.getByName(newFolder.getGroupName()).isPresent()) {
                Group group = groupRepository.getByName(newFolder.getGroupName()).orElse(new Group(newFolder.getGroupName()));
                group = groupRepository.saveAndFlush(group);

                Permission permission = new Permission();
                permission.setFolder(folder);
                permission.setGroup(group);
                if (Objects.equals(newFolder.getAccess(), "readwrite")) {
                    permission.setRights(Rights.WRITE);
                } else {
                    permission.setRights(Rights.READ);
                }
                permissionRepository.save(permission);
            } else {
                Group group = new Group();
                if (newFolder.getGroupName().isBlank()) {
                    newFolder.setGroupName("All");
                    group = groupRepository.getByName("All").get();
                } else {
                    group = groupRepository.saveAndFlush(new Group(newFolder.getGroupName()));
                }

                User user = principleService.getPrincipalUser();

                if (user.getGroups() == null) {
                    user.setGroups(new HashSet<>());
                }
                user.getGroups().add(group);
                userRepository.save(user);

                Permission permission = new Permission();
                permission.setFolder(folder);
                permission.setGroup(group);
                if (Objects.equals(newFolder.getAccess(), "readwrite")) {
                    permission.setRights(Rights.WRITE);
                } else {
                    permission.setRights(Rights.READ);
                }
                permissionRepository.save(permission);

            }
        }

        return folder;
    }

    public Folder addRootFolder() {
        String namespaceName = "RootFolder";
        if (folderRepository.findByName(namespaceName) == null) {
            folderRepository.createRootFolder(namespaceName);

            Folder folder = folderRepository.findByName(namespaceName);

            Group group;

            if (groupRepository.getByName("All").isEmpty()) {
                group = new Group("All");
                group = groupRepository.saveAndFlush(group);
            } else {
                group = groupRepository.getByName("All").get();
            }

            Permission permission = new Permission();
            permission.setFolder(folder);
            permission.setGroup(group);
            permission.setRights(Rights.WRITE);
            permissionRepository.save(permission);
        }

        return folderRepository.findByName(namespaceName);
    }

    public File addFile(Long id, File file) {
        Folder folder = folderRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        FileImport fileImport = new FileImport();
        fileImport.setFile(file);
        fileImport.setUser(principleService.getPrincipalUser());
        fileImport.setTime(LocalDateTime.now());
        file.setFileImport(fileImport);

        FileModification fileModification = new FileModification();
        fileModification.setFile(file);
        fileModification.setUser(principleService.getPrincipalUser());
        fileModification.setTime(LocalDateTime.now());
        file.setFileModification(fileModification);

        file.setFolder(folder);
        if (folder.getFolders() == null) {
            folder.setFolders(new HashSet<>());
        }
        folder.getFiles().add(file);

        return fileRepository.save(file);
    }

    public List<FolderInFolder> getAllFoldersInFolder(Long id) {
        List<Folder> folders = folderRepository.findAllByRootFolderId(id);
        folders = folders.stream().filter(folder -> folder.getId() != 1L).collect(Collectors.toList());

        List<FolderInFolder> folderInFolderList = new ArrayList<>();

        List<Group> userGroups = new ArrayList<>(principleService.getPrincipalUser().getGroups());
        List<Folder> userFolders = new ArrayList<>();
        for (Folder f : folders) {
            List<Permission> permissions = permissionRepository.getAllByFolderId(f.getId());
            for (Permission p : permissions) {
                if (userGroups.contains(p.getGroup())) {
                    userFolders.add(f);
                    break;
                }
            }
        }

        userFolders.forEach(f -> {
            FolderInFolder folderInFolder = new FolderInFolder();
            folderInFolder.setFolder(f);
            if (f.getFiles().isEmpty()) {
                folderInFolder.setFileModification(null);
            } else {
                FileModification fileModification = f.getFiles().stream()
                        .max(Comparator.comparingInt(file -> file.getFileModification().getTime().getNano()))
                        .get().getFileModification();
                folderInFolder.setFileModification(fileModification);
                folderInFolder.setUsername(fileModification.getUser().getUsername());
            }

            folderInFolderList.add(folderInFolder);
        });

        return folderInFolderList;
    }

    public List<FileInFolder> getAllFilesInFolder(Long id) {
        List<File> files = fileRepository.findAllByFolderId(id);
        List<FileInFolder> fileInFolderList = new ArrayList<>();

        files.forEach(f -> {
            FileInFolder fileInFolder = new FileInFolder();
            fileInFolder.setFile(f);
            fileInFolder.setFileModification(f.getFileModification());
            fileInFolder.setUsername(f.getFileModification().getUser().getUsername());
            fileInFolderList.add(fileInFolder);
        });

        return fileInFolderList;
    }

    public ObjectsInFolder getObjectsInFolder(Long id) {
        ObjectsInFolder objectsInFolder = new ObjectsInFolder();
        objectsInFolder.setFolderInFolderList(getAllFoldersInFolder(id));
        objectsInFolder.setFileInFolderList(getAllFilesInFolder(id));
        return objectsInFolder;
    }

    public List<Permission> getPermissions(Long id) {
        return permissionRepository.getAllByFolderId(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}
