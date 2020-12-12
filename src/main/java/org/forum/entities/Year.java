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


}
