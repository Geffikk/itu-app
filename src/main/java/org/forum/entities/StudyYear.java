package org.forum.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rocnik")
public class StudyYear {

    /** ID ROCNIKA **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** NAZOV ROCNIKA **/
    @Column(name = "nazov", length = 50)
    private String name;

    /** ROCNIK PATRI DO ROKU **/
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_roku")
    private Year year;

    /** CONSTRUCTORS **/
    public StudyYear() {
    }

    public StudyYear(String name) {
        this.name = name;
    }

    /** GETTERS AND SETTERS **/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
