package com.example.storeeverything;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
 
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
 
    @Autowired
    private TestEntityManager entityManager;
     
    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;
     
    // test methods go below

    @Test
    public void testAddRoles() {
        Role admin = new Role();
        admin.setRoleName("Admin");

        Role fullUser = new Role();
        fullUser.setRoleName("FullUser");

        Role limitedUser = new Role();
        limitedUser.setRoleName("LimitedUser");

        List<Role> roles = new ArrayList<>();
        roles.add(admin);
        roles.add(limitedUser);
        roles.add(fullUser);

        assertThat(repo.findAll().size()).isEqualTo(roles.size());
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("paszkos998@gmail.com");
        user.setPassword("admin1");
        user.setFirstName("Kamil");
        user.setLastName("Paszkos");

        Set<Role> roles = new HashSet<>(roleRepo.findAll());
        user.setRoles(roles);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        User savedUser = repo.save(user);
        
        User existUser = entityManager.find(User.class, savedUser.getId());
        
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
    }
}