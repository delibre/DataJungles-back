package com.delibre.datajungles.controller.dataset;

import com.delibre.datajungles.model.File;
import com.delibre.datajungles.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/dataset")
//@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class DatasetController {

    @Autowired
    private FileService fileService;

    @GetMapping("{id}")
    public ResponseEntity<File> getDataset(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFile(id));
    }

    @PutMapping("{id}/update")
    public ResponseEntity<File> updateDataset(@PathVariable Long id, @RequestBody File file) {
        file.setId(id);
        return ResponseEntity.ok(fileService.updateFile(file));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteDataset(@PathVariable String id) {
        return ResponseEntity.ok(fileService.deleteFile(Long.valueOf(id)));
    }

}
