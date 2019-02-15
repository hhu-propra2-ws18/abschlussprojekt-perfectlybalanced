package de.hhu.abschlussprojektverleihplattform;

import de.hhu.abschlussprojektverleihplattform.model.Role;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.repository.UserRepository;
import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CookieUserServiceTest {

    @Autowired
    CookieUserService cookieUserService;

    @Autowired
    UserRepository userService;

    @Test
    public void can_get_user_from_cookie() throws Exception{

        UserEntity user1 = new UserEntity();
        user1.setFirstname("thomas");
        user1.setLastname("jenkins");
        user1.setUsername("user1");
        user1.setPassword("password");
        user1.setEmail("me@hello.de");
        user1.setRole(Role.user);
        userService.saveUser(user1);

        //make a query to try to commit our change
        try {
            userService.findById(1L);
        }catch (Exception e){
	//TODO
	}

        Long userId = userService.getUserByFirstname("thomas").getUserId();

        System.out.println(userId);

        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setCookies(
	    new Cookie(CookieUserService.cookieName,""+userId)
	);

        UserEntity user2 = cookieUserService.getUserFromRequest(request);

        Assert.assertEquals(user2.getFirstname(),"thomas");
    }
}
