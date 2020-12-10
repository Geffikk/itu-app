package org.forum.entities.user;

import org.forum.entities.Section;
import org.forum.entities.StudyYear;
import org.forum.entities.Topic;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "uzivatel")
public class User {

    /** ID UZIVATELA **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** EMAIL UZIVATELA **/
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /** MENO UZIVATELA **/
    @Column(name = "uzivatelske_meno", length = 50, nullable = false, unique = true)
    private String username;

    /** HESLO UZIVATELA **/
    @Column(name = "heslo", length = 60, nullable = false)
    private String password;

    @Column(name = "pocet_bodov")
    private int points;

    @Column(name = "pocet_penazi")
    private int money;

    @Column(name = "datum_registarcie")
    private Date createdAt;

    @Column(name = "posledny_datum_prihlasenia")
    private Date lastLoginTime;

    @Column(name = "rola")
    private String role = "USER";

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uzivatelske_info_id")
    private UserAdditionalInfo info;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "oblubene_vlakna",
            joinColumns = @JoinColumn(name = "id_uzivatela"),
            inverseJoinColumns = @JoinColumn(name = "id_vlakna")
    )
    private Set<Topic> favoriteTopics;


    /** CONSTRUCTORS **/
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public User() {
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /** GETTERS AND SETTERS **/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String  getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public UserAdditionalInfo getInfo() {
        return info;
    }

    public void setInfo(UserAdditionalInfo info) {
        this.info = info;
    }

    public Set<Topic> getFavoriteTopics() {
        return favoriteTopics;
    }

    public void setFavoriteTopics(Set<Topic> favoriteTopics) {
        this.favoriteTopics = favoriteTopics;
    }

    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }


    public void addFavoriteTopic(Topic topic){
        if(favoriteTopics == null){
            favoriteTopics = new HashSet<>();
        }
        favoriteTopics.add(topic);
    }
}


