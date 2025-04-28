package com.rrtyui.filestorage.service;


import com.rrtyui.filestorage.dto.MyUserRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    public void signIn(MyUserRequestDto myUserRequestDto) {
        var token = createToken(myUserRequestDto);
        var authentication = authenticationManager.authenticate(token);

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
    }

    private UsernamePasswordAuthenticationToken createToken(MyUserRequestDto dto) {
        return new UsernamePasswordAuthenticationToken(
                dto.getUsername(), dto.getPassword()
        );
    }
}
