package com.eg.blps1.utils;

import com.eg.blps1.model.User;
import com.eg.blps1.service.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonUtils {
    public static User getUserFromSecurityContext() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
}
