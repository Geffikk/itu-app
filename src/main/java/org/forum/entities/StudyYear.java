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

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
           name = "rocnik_rok",
           joinColumns = @JoinColumn(name = "id_rocnika"),
           inverseJoinColumns = @JoinColumn(name = "id_roku")
    )
    private List<Year> years;

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

    public List<Year> getYears() {
        return years;
    }

    public void setYears(List<Year> years) {
        this.years = years;
    }

    public void addYear(Year year){
        if(years == null){
            years = new ArrayList<>();
        }
        years.add(year);
    }
}
