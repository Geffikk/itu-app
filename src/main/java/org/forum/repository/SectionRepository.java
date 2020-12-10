package org.forum.repository;

import org.forum.entities.Section;
import org.forum.entities.StudyYear;
import org.forum.entities.Topic;
import org.forum.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    Section findByName(String name);

    List<Section> findByStudyYear(StudyYear studyYear);
}
