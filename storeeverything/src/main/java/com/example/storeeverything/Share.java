package com.example.storeeverything;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Share")
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(targetEntity = User.class)
    private User user;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(targetEntity = Category.class)
    private Category category;

    @Column(nullable = true, length = 128)
    private String link;

    @Column(nullable = false, name = "share_date")
    private Date shareDate;

    @Column(name = "description", nullable = true, length = 256)
    private String desc;

    // getter 
    public Long getId() {
        return this.id;
    }

    public User getUser() {
       return this.user;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getDesc() {
        return this.desc;
    }
    
    public String getLink() {
        return this.link;
    }

    public Date getShareDate() {
        return this.shareDate;
    }

    public String getTitle() {
        return this.title;
    }

    // setter
    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setShareDate(Date date) {
        this.shareDate = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
