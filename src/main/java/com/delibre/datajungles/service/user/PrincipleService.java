package com.delibre.datajungles.service.user;

import com.delibre.datajungles.model.dto.UserPrincipleData;
import com.delibre.datajungles.model.user.User;
import com.delibre.datajungles.model.user.UserDetailsImpl;
import com.delibre.datajungles.repository.GroupRepository;
import com.delibre.datajungles.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PrincipleService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    public PrincipleService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    private long getId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    public User getPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserPrincipleData> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserPrincipleData> usersData = new ArrayList<>();

        for (User user : users) {
            usersData.add(new UserPrincipleData(user.getUsername(), user.getId()));
        }

        return usersData;
    }

    public List<UserPrincipleData> getAllUsersForGroup(Long id) {
        List<User> users = userRepository.findAll();
        List<UserPrincipleData> usersData = new ArrayList<>();

        for (User user : users) {
            user.getGroups().forEach(g -> {
                if (Objects.equals(g.getId(), id)) {
                    usersData.add(new UserPrincipleData(user.getUsername(), user.getId()));
                }
            });
        }

        return usersData;
    }

    public List<UserPrincipleData> getAllUsersForGroupToAdd(Long id) {
        List<User> users = userRepository.findAll();
        List<UserPrincipleData> usersInGroup = getAllUsersForGroup(id);
        List<UserPrincipleData> usersData = new ArrayList<>();

        users = users.stream().filter(u ->
                usersInGroup.stream().noneMatch(ud -> ud.getUsername().equals(u.getUsername()))
        ).collect(Collectors.toList());

        for (User user : users) {
            usersData.add(new UserPrincipleData(user.getUsername(), user.getId()));
        }

        return usersData;
    }

    public Long addUserToGroup(User user, Long groupId) {

        User userToAdd = userRepository.findByUsername(user.getUsername()).get();

        userToAdd.getGroups().add(groupRepository.findById(groupId).get());

        userRepository.saveAndFlush(userToAdd);

        return userToAdd.getId();
    }
}
