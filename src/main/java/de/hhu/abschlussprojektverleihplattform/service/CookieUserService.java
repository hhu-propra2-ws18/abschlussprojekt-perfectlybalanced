package de.hhu.abschlussprojektverleihplattform.service;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUserService {

    @Autowired
    UserRepository userService;

    public static final String cookieName="userId";

    public UserEntity getUserFromRequest(HttpServletRequest request) throws Exception{

        for(Cookie  cookie : request.getCookies()){
            if(cookie.getName().equals(cookieName)){
                return userService.findById(Long.parseLong(cookie.getValue()));
            }
        }
        throw new Exception("could not find user from cookie");
    }
}
