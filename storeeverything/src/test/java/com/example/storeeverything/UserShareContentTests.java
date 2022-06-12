package com.example.storeeverything;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
 
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserShareContentTests {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ShareRepository repo;

    @Autowired
    private CategoryRepository categoryRepo;


    @Test
    public void testCreateCategories() {
        List<Category> categoryList = new ArrayList<>();

        var c = new Category();
        c.setName("Generic");
        categoryList.add(c);

        c = new Category();
        c.setName("Entertainment");
        categoryList.add(c);

        c = new Category();
        c.setName("Science");
        categoryList.add(c);

        categoryRepo.saveAll(categoryList);

        assertThat(categoryRepo.findAll().size()).isEqualTo(categoryList.size());
    }

    @Test
    public void testShare() {
        Optional<User> optional = userRepo.findById((long) 1);
        User user = optional.orElse(null);

        assertThat(user).isNotEqualTo(null);

        List<Category> categoryList = categoryRepo.findAll();

        var count = repo.count();

        Share share = new Share();
        share.setTitle("Example #1 - Test");
        share.setCategory(categoryList.get(0));
        share.setLink("https://wi.pb.edu.pl/");
        share.setDesc("Wydział Informatyki Politechniki Białostockiej");
        share.setShareDate(new Date());
        share.setUser(user);

        repo.save(share);

        assertThat(repo.count()).isEqualTo(count+1);
    }
}
