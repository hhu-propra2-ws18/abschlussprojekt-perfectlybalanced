package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUserService {

    @Autowired
    UserService userService;

    public UserEntity getUserFromRequest(HttpServletRequest request) throws Exception{

        for(Cookie  cookie : request.getCookies()){
            if(cookie.getName().equals("username")){
                return userService.showUserById(Long.parseLong(cookie.getValue()));
            }
        }
        throw new Exception("could not find user from cookie");
    }
}
