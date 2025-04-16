package com.rrtyui.filestorage.service;


import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.exception.UserAlreadyExistException;
import com.rrtyui.filestorage.mapper.MyUserMapper;
import com.rrtyui.filestorage.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    @Transactional
    public void register(MyUserRequestDto myUserRequestDto) {

        if (userRepository.existsByName(myUserRequestDto.getName())) {
            throw new UserAlreadyExistException("User with this name is exist, please select another one");
        }

        String encode = passwordEncoder.encode(myUserRequestDto.getPassword());
        myUserRequestDto.setPassword(encode);
        MyUser myUser = MyUserMapper.toMyUser(myUserRequestDto);

        userRepository.saveAndFlush(myUser);
    }

    public void signIn(MyUserRequestDto myUserRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                myUserRequestDto.getName(), myUserRequestDto.getPassword()
        ));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }
}
