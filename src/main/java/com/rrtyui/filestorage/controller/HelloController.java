package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.entity.MyUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "This is unprotected page";
    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public String sayHelloToUser() {
        return "Page for user";
    }

    @GetMapping("/userInfo")
    public String showInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        System.out.println(principal);

        return "";
    }
}
