package org.forum.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rok")
public class Year {


    /** ID ROCNIKA **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** CISLO ROKU **/
    @Column(name = "nazov", length = 50)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "rocnik_rok",
            joinColumns = @JoinColumn(name = "id_roku"),
            inverseJoinColumns = @JoinColumn(name = "id_rocnika")
    )
    private Set<StudyYear> studyYears;



    /** CONSTRUCTORS **/
    public Year() {
    }

    public Year(String name) {
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

    public Set<StudyYear> getStudyYears() {
        return studyYears;
    }

    public void setStudyYears(Set<StudyYear> studyYears) {
        this.studyYears = studyYears;
    }

    public void addStudyYear(StudyYear studyYear){
        if(studyYears == null){
            studyYears = new HashSet<>();
        }
        studyYears.add(studyYear);
    }
}
