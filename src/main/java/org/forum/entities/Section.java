package org.forum.entities;


import javax.persistence.*;


@Entity
@Table(name = "skupina")
public class Section {

    /** ID SKUPINY**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** NAZOV SKUPINY **/
    @Column(name = "nazov", length = 50)
    private String name;


    /** SKUPINA PATRI DO ROCNIKA **/
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_rocnika")
    private StudyYear studyYear;


    /** CONSTRUCTORS **/
    public Section() {
    }

    public Section(String nazov) {
        this.name = nazov;
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

    public StudyYear getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(StudyYear studyYear) {
        this.studyYear = studyYear;
    }
}
