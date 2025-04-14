package com.rrtyui.filestorage.security;

import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> myUser = userRepository.findByName(username);

        return myUser.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("This user doesn't exist:" + username));
    }
}
