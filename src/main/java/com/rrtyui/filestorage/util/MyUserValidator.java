package com.rrtyui.filestorage.util;

import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MyUserValidator implements Validator {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public MyUserValidator(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MyUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MyUser myUser = (MyUser) target;

        try {
            myUserDetailsService.loadUserByUsername(myUser.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("username","", "Имя занято");
    }
}
