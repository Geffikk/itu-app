package org.forum.service;

import org.forum.entities.Section;
import org.forum.entities.StudyYear;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface SectionService {

    List<Section> findAll();

    Section findOne(int id);

    Section findByName(String name);

    List<Section> findByStudyYear(StudyYear studyYear);

    Section save(Section section);

    void delete(int id);

    void delete(Section section);

}
