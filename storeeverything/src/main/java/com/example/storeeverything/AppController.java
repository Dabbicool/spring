package com.example.storeeverything;
 
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class AppController {

    @Autowired
    private EntityManager entityManager;
 
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ShareRepository shareRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping("/share/add")
    public String showShareAddForm(Model model) {
        model.addAttribute("share", new Share());
        model.addAttribute("categoryList", categoryRepo.findAll());

        return "add_share";
    }

    @PostMapping("/share/process")
    public String processSharing(Share share, @AuthenticationPrincipal CustomUserDetails userDetails) {
        share.setShareDate(new Date());
        share.setUser(userDetails.getUser());

        if(share.getUser() != null)
            shareRepo.save(share);

        return "redirect:/";
    }

    @GetMapping("/share/{id}")
    public String viewShare(Model model, @PathVariable Long id) {
        Optional<Share> share = shareRepo.findById(id);

        if(share.isPresent()) {
            model.addAttribute("share", share.get());

            return "share_id";
        }

        return "error-generic";
    }

    @GetMapping("/share/user/{id}")
    public String viewUserShares(Model model, @PathVariable Long id) {
        Optional<User> user = userRepo.findById(id);

        if(!user.isPresent())
            return "error-generic";

        List<Share> shareList = shareRepo.findByUser(user.get());
        model.addAttribute("shareList", shareList);
        model.addAttribute("user", user.get());

        return "user_shares";
    }
     
    @GetMapping(value = {"", "/", "/welcome"})
    public String viewHomePage(Model model, @RequestParam Optional<Long> q) {
        model.addAttribute("shareList", shareRepo.getLastShares(q.orElse((long) 25)));
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        
        return "signup_form";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
        
        return "admin";
    }

    @GetMapping("/admin/edit/user/{id}")
    public String adminEditUser(@PathVariable Long id, Model model) {
        Optional<User> optional = userRepo.findById(id);

        if(optional.isPresent())
            model.addAttribute("user", optional.get());
            
        model.addAttribute("listRoles", roleRepo.findAll());

        return "admin_edit_user";
    }

    @PostMapping("/admin/edit/user/process")
    public String adminEditUserProcess(User user) {
        userRepo.save(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/user/{id}")
    @Transactional
    public String adminDeleteUser(@PathVariable Long id, Model model) {
        Optional<User> optional = userRepo.findById(id);
        User user = optional.orElse(null);

        if(user != null)
        {
            for(Role role : user.getRoles())
                user.getRoles().remove(role);

            String sqlString = "DELETE FROM user_roles ur WHERE ur.userd_id = ?1";
            Query query = entityManager.createNativeQuery(sqlString);
            query.setParameter(1, user.getId());
            query.executeUpdate();

            userRepo.delete(user);
        }

        return "redirect:/admin";
    }

    @GetMapping("/category/add")
    public String categoryAddForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categoryList", categoryRepo.findAll());

        return "category_add_form";
    }

    @PostMapping("/category/add")
    public String categoryAddFormProcess(Category category) {
        categoryRepo.save(category);

        return "redirect:/";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role role = roleRepo.findByRoleName("LimitedUser");

        if(role != null)
        {
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }
        
        userRepo.save(user);
        
        return "register_success";
    }
}