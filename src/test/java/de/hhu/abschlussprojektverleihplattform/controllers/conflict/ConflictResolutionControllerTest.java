package de.hhu.abschlussprojektverleihplattform.controllers.conflict;

import de.hhu.abschlussprojektverleihplattform.model.LendingEntity;
import de.hhu.abschlussprojektverleihplattform.model.ProductEntity;
import de.hhu.abschlussprojektverleihplattform.model.Role;
import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import de.hhu.abschlussprojektverleihplattform.security.AuthenticatedUserService;
import de.hhu.abschlussprojektverleihplattform.service.LendingService;
import de.hhu.abschlussprojektverleihplattform.service.UserService;
import de.hhu.abschlussprojektverleihplattform.utils.RandomTestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConflictResolutionControllerTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticatedUserService authenticatedUserService;

    @MockBean
    UserService userService;

    @MockBean
    LendingService lendingService;

    private Random randomID = new Random();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void conflictCenterIsOk() throws Exception {
        UserEntity admin = createAdmin();

        String username = admin.getUsername();
        when(userService.findByUsername(username))
            .thenReturn(admin);

        mockMvc
            .perform(get("/conflictcenter")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Aktuelle Konflikte")));
    }

    @Test
    public void conflictCenterIsForbiddenForUser() throws Exception {
        UserEntity user = RandomTestData.newRandomTestUser();
        user.setUserId(randomID.nextLong());

        String username = user.getUsername();
        when(userService.findByUsername(username))
            .thenReturn(user);

        mockMvc
            .perform(get("/conflictcenter")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void conflictCenterShowConflict() throws Exception {
        UserEntity admin = createAdmin();
        LendingEntity lendingConflict = createConflictItem(randomID.nextLong());
        String username = admin.getUsername();
        List<LendingEntity> allConflicts = new ArrayList<>();
        allConflicts.add(lendingConflict);

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getAllConflicts()).thenReturn(allConflicts);

        mockMvc
            .perform(get("/conflictcenter")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Aktuelle Konflikte")))
            .andExpect(content().string(containsString(lendingConflict.getProduct().getTitle())));
    }

    @Test
    public void conflictCenterShowsTwoConflicts() throws Exception {
        UserEntity admin = createAdmin();
        LendingEntity lendingConflict = createConflictItem(randomID.nextLong());
        LendingEntity lendingConflict2 = createConflictItem(randomID.nextLong());
        String username = admin.getUsername();

        List<LendingEntity> allConflicts = new ArrayList<>();
        allConflicts.add(lendingConflict);
        allConflicts.add(lendingConflict2);

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getAllConflicts())
            .thenReturn(allConflicts);

        mockMvc
            .perform(get("/conflictcenter")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content()
                .string(containsString("Aktuelle Konflikte")))
            .andExpect(content()
                .string(containsString(lendingConflict
                    .getProduct()
                    .getTitle())))
            .andExpect(content()
                .string(containsString(lendingConflict2
                    .getProduct()
                    .getTitle())));
    }

    @Test
    public void conflictCenterShowsNoConflict() throws Exception {
        UserEntity admin = createAdmin();
        String username = admin.getUsername();

        List<LendingEntity> allConflicts = new ArrayList<>();

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getAllConflicts())
            .thenReturn(allConflicts);

        mockMvc
            .perform(get("/conflictcenter")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("keine Konflikte")));
    }

    @Test
    public void conflictCenterShowDetail() throws Exception {
        UserEntity admin = createAdmin();
        LendingEntity lendingConflict = createConflictItem(randomID.nextLong());
        String username = admin.getUsername();

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getLendingById(ArgumentMatchers.anyLong()))
            .thenReturn(lendingConflict);

        mockMvc
            .perform(get("/conflictcenter/conflictdetail/" + lendingConflict.getId().toString())
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content()
                .string(containsString(lendingConflict
                    .getProduct()
                    .getTitle())))
            .andExpect(content()
                .string(containsString(lendingConflict
                    .getBorrower()
                    .getUsername())))
            .andExpect(content()
                .string(containsString(lendingConflict
                    .getProduct()
                    .getOwner()
                    .getUsername())));
    }

    @Test
    public void conflictCenterDetailRedirectWhenLendingIsNull() throws Exception {
        UserEntity admin = createAdmin();
        LendingEntity lendingConflict = createConflictItem(randomID.nextLong());
        String username = admin.getUsername();

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getLendingById(ArgumentMatchers.anyLong()))
            .thenReturn(null);

        mockMvc
            .perform(get("/conflictcenter/conflictdetail/" + lendingConflict.getId().toString())
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void decideForOwner() throws Exception {
        UserEntity admin = createAdmin();
        LendingEntity lendingConflict = createConflictItem(randomID.nextLong());
        String username = admin.getUsername();

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getLendingById(ArgumentMatchers.anyLong()))
            .thenReturn(lendingConflict);
        Mockito
            .doNothing()
            .when(lendingService)
            .ownerReceivesSuretyAfterConflict(lendingConflict);

        mockMvc
            .perform(post("/conflictcenter/decideForOwner?id=" + lendingConflict.getId().toString())
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void decideForOwnerNotFound() throws Exception {
        UserEntity admin = createAdmin();
        String username = admin.getUsername();

        when(userService.findByUsername(username))
            .thenReturn(admin);
        when(lendingService.getLendingById(ArgumentMatchers.anyLong()))
            .thenReturn(null);

        mockMvc
            .perform(get("/conflictcenter/decideForOwner?id=10")
                .with(user(authenticatedUserService.loadUserByUsername(username)))
                .with(csrf()))
            .andExpect(status().is4xxClientError());
    }


    private UserEntity createAdmin() {
        UserEntity admin = RandomTestData.newRandomTestUser();
        admin.setUserId(randomID.nextLong());
        admin.setRole(Role.ROLE_ADMIN);
        return admin;
    }

    private LendingEntity createConflictItem(Long id) {
        UserEntity user = RandomTestData.newRandomTestUser();
        user.setUserId(id);
        ProductEntity product = RandomTestData
            .newRandomTestProduct(user, RandomTestData
                .newRandomTestAddress());
        product.setId(id);
        LendingEntity lendingConflict = RandomTestData
            .newRandomLendingStatusConflict(user, product);
        lendingConflict.setId(id);
        return lendingConflict;
    }

}
