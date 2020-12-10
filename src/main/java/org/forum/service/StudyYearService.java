package org.forum.service;

import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface StudyYearService {

    List<StudyYear> findAll();

    StudyYear findOne(int id);

    StudyYear findByName(String name);


    StudyYear save(StudyYear studyYear);

    void delete(int id);

    void delete(StudyYear studyYear);
}
