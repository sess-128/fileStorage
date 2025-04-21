package com.rrtyui.filestorage.service;

import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.repository.UserRepository;
import com.rrtyui.filestorage.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<MyUser> user = userRepository.findByName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("adklamnsldkasd");
        }

        return new MyUserDetails(user.get());
    }

}
