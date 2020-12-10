package org.forum.service;

import org.forum.entities.Section;
import org.forum.entities.StudyYear;
import org.forum.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;


    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public Section findOne(int id) {
        Optional<Section> optional = sectionRepository.findById(id);
        Section section = null;
        if (optional.isPresent()) {
            section = optional.get();
        } else {
            throw new RuntimeException("Skupina s id: " + id + " nebola najdena!");
        }
        return section;
    }

    @Override
    public Section findByName(String nazov) {
        return sectionRepository.findByName(nazov);
    }

    @Override
    public List<Section> findByStudyYear(StudyYear studyYear) {
        return sectionRepository.findByStudyYear(studyYear);
    }

    @Override
    public Section save(Section section) {
        return sectionRepository.save(section);
    }


    @Override
    public void delete(int id) {
        delete(findOne(id));
    }

    @Override
    public void delete(Section section) {
        sectionRepository.delete(section);
    }

}