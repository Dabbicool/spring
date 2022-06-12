package com.example.storeeverything;
 
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
 
@Entity
@Table(name = "users")
public class User {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    @Column(nullable = false, unique = true, length = 45)
    private String email;
     
    @Column(nullable = false, length = 64)
    private String password;
     
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;
     
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name="user_roles", joinColumns = @JoinColumn (name="userd_id"), inverseJoinColumns = @JoinColumn (name="role_id"))
    private Set<Role> roles = new HashSet<>();

    // getters and setters 
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public void setPassword(String passwd) {
        this.password = passwd;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // getters

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }
}