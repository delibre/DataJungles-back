package com.delibre.datajungles.service.user;

import com.delibre.datajungles.model.user.Group;
import com.delibre.datajungles.model.user.Role;
import com.delibre.datajungles.model.user.User;
import com.delibre.datajungles.model.user.UserDetailsImpl;
import com.delibre.datajungles.repository.GroupRepository;
import com.delibre.datajungles.repository.RoleRepository;
import com.delibre.datajungles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PrincipleService principleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public List<Group> getGroups() {
        List<Group> allGroups = groupRepository.findAll();
        return allGroups.stream().filter(group -> !group.getName().startsWith("private")).collect(Collectors.toList());
    }

    public List<Group> getUserGroups() {
        return new ArrayList<>(principleService.getPrincipalUser().getGroups());
    }
}