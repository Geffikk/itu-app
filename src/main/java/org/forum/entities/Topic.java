package org.forum.entities;


import org.forum.entities.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vlakno")
public class Topic {

    /** ID VLAKNA **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** ZAKLADATEL VLAKNA **/
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_uzivatela")
    private User user;

    /** VLAKNO PATRI DO SKUPINY **/
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_skupiny")
    private Section section;


    /** PREDMET VLAKNA **/
    @Column(name = "predmet", length = 50)
    private String title;

    @Column(name = "kontent", columnDefinition = "TEXT")
    private String content;

    @Column(name = "datum_zalozenia", updatable = false, nullable = false)
    private Date creationDate;

    @Column(name = "posledny_datum_upravy")
    private Date lastUpdateDate;

    @Column(name = "je_zavrete")
    private boolean closed;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "oblubene_vlakna",
            joinColumns = @JoinColumn(name = "id_vlakna"),
            inverseJoinColumns = @JoinColumn(name = "id_uzivatela")
    )
    private Set<User> usersLikedThisTopic;


    /** CONSTRUCTORS **/
    public Topic() {
    }

    public Topic(String predmet, String kontent) {
        this.content = kontent;
        this.title = predmet;
    }



    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
        this.lastUpdateDate = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdateDate = new Date();
    }



    /** GETTERS AND SETTERS **/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

}
