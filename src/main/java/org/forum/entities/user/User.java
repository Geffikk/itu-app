package org.forum.entities.user;

import org.forum.entities.Post;
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

    @Column(name = "precitane_skupiny")
    private String readTopics = "";

    @Column(name = "upozornenia")
    private String notifications = "";

    @Column(name = "upozornenia_id")
    private String notifications_id = "";

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
    private List<Topic> favoriteTopics;


    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "uzivatel_like_prispevok",
            joinColumns = @JoinColumn(name = "id_uzivatela"),
            inverseJoinColumns = @JoinColumn(name = "id_prispevku")
    )
    private List<Post> likedPosts;


    /** CONSTRUCTORS **/
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public User() {
    }

    public User(String email, String username, String password, int points, int money) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.points = points;
        this.money = money;
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

    public String getNotifications_id() {
        return notifications_id;
    }

    public void setNotifications_id(String notifications_id) {
        this.notifications_id = notifications_id;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMoney() {
        return money;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
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

    public List<Topic> getFavoriteTopics() {
        return favoriteTopics;
    }

    public void setFavoriteTopics(List<Topic> favoriteTopics) {
        this.favoriteTopics = favoriteTopics;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public String getReadTopics() {
        return readTopics;
    }

    public void setReadTopics(String readTopics) {
        this.readTopics = readTopics;
    }

    public List<String> getRoleList() {
        List<String> temp_permissions = new ArrayList<>();

        if(this.role.length() > 0) {
            temp_permissions = Arrays.asList(this.role.split(","));

            return new ArrayList<>(temp_permissions);
        }
        return new ArrayList<>();
    }

    public String roleFromListToString(List<String> list) {
        StringBuilder not = new StringBuilder();
        int size = list.size();

        for (String item : list) {
            size = size - 1;
            not.append(item);

            if (size != 0)
                not.append(',');
        }
        return not.toString();
    }

    public List<String> getNotificationList() {
        List<String> temp_permissions = new ArrayList<>();

        if(this.notifications.length() > 0) {
            temp_permissions = Arrays.asList(this.notifications.split(","));

            return new ArrayList<>(temp_permissions);
        }
        return new ArrayList<>();
    }


    public String notificationFromListToString(List<String> list) {
        StringBuilder not = new StringBuilder();
        int size = list.size();

        for (String item : list) {
            size = size - 1;
            not.append(item);

            if (size != 0)
                not.append(',');
        }
        return not.toString();
    }

    public List<String> getNotificationListId() {
        List<String> temp_permissions = new ArrayList<>();

        if(this.notifications_id.length() > 0) {
            temp_permissions = Arrays.asList(this.notifications_id.split(","));

            return new ArrayList<>(temp_permissions);
        }
        return new ArrayList<>();
    }

    public String notificationFromListToStringId(List<String> list) {
        StringBuilder not = new StringBuilder();
        int size = list.size();

        for (String item : list) {
            size = size - 1;
            not.append(item);

            if (size != 0)
                not.append(',');
        }
        return not.toString();
    }

    public void addFavoriteTopic(Topic topic){
        if(favoriteTopics == null){
            favoriteTopics = new ArrayList<>();
        }
        favoriteTopics.add(topic);
    }

    public void addLikedPost(Post post){
        if(likedPosts == null){
            likedPosts = new ArrayList<>();
        }
        likedPosts.add(post);
    }

    public List<String> getReadTopicsList() {
        if(this.readTopics.length() > 0) {
            return Arrays.asList(this.readTopics.split(","));
        }
        return new ArrayList<>();
    }
}


