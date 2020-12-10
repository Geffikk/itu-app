package org.forum.repository;

import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StudyYearRepository extends JpaRepository<StudyYear, Integer> {

    StudyYear findByName(String name);


}
