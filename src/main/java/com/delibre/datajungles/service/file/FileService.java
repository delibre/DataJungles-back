package com.delibre.datajungles.service.file;

import com.delibre.datajungles.model.File;
import com.delibre.datajungles.model.FileModification;
import com.delibre.datajungles.repository.FileImportRepository;
import com.delibre.datajungles.repository.FileModificationRepository;
import com.delibre.datajungles.repository.FileRepository;
import com.delibre.datajungles.service.user.PrincipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileModificationRepository fileModificationRepository;

    @Autowired
    private FileImportRepository fileImportRepository;

    @Autowired
    private PrincipleService principleService;

    public File getFile(Long id) {
        return fileRepository.findById(id).get();
    }

    public File updateFile(File file) {
        File foundFile = fileRepository.findById(file.getId()).orElseThrow(ResourceNotFoundException::new);
        foundFile.setDataset(file.getDataset());

        FileModification fileModification = fileModificationRepository.findByFileId(foundFile.getId());
        fileModification.setFile(foundFile);
        fileModification.setUser(principleService.getPrincipalUser());
        fileModification.setTime(LocalDateTime.now());
        foundFile.setFileModification(fileModification);

        return fileRepository.save(foundFile);
    }

    @Transactional
    public Long deleteFile(Long id) {
        fileModificationRepository.myDelete(id);
        fileImportRepository.myDelete(id);
        fileRepository.myDelete(id);

        return id;
    }
}
