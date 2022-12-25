package com.delibre.datajungles.controller.user;


import com.delibre.datajungles.model.dto.UserPrincipleData;
import com.delibre.datajungles.model.user.Group;
import com.delibre.datajungles.model.user.Role;
import com.delibre.datajungles.model.user.User;
import com.delibre.datajungles.service.folder.FolderService;
import com.delibre.datajungles.service.user.PrincipleService;
import com.delibre.datajungles.service.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private PrincipleService principleService;
    @Autowired
    private FolderService folderService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(userDetailsServiceImpl.getRoles());
    }

    @GetMapping("/user-groups")
    public ResponseEntity<List<Group>> getGroups() {
        return ResponseEntity.ok(userDetailsServiceImpl.getUserGroups());
    }

    @GetMapping("/all-groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(folderService.getAllGroups());
    }

    @GetMapping("/usersdata")
    public ResponseEntity<List<UserPrincipleData>> getUsersData() {
        return ResponseEntity.ok(principleService.getUsers());
    }

    @GetMapping("all-users-for-group/{id}")
    public ResponseEntity<List<UserPrincipleData>> getAllUsersForGroup(@PathVariable Long id) {
        return ResponseEntity.ok(principleService.getAllUsersForGroup(id));
    }

    @GetMapping("all-users-for-group-to-add/{id}")
    public ResponseEntity<List<UserPrincipleData>> getAllUsersForGroupToAdd(@PathVariable Long id) {
        return ResponseEntity.ok(principleService.getAllUsersForGroupToAdd(id));
    }

    @PostMapping("add-user-to-group/{id}")
    public ResponseEntity<Long> addUserToGroup(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(principleService.addUserToGroup(user, id));
    }
}
