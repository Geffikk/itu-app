package org.forum.service;

import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.forum.repository.StudyYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudyYearServiceImpl implements StudyYearService {

    @Autowired
    private StudyYearRepository studyYearRepository;


    @Override
    public List<StudyYear> findAll() {
        return studyYearRepository.findAll();
    }

    @Override
    public StudyYear findOne(int id) {
        Optional<StudyYear> optional = studyYearRepository.findById(id);
        StudyYear studyYear = null;
        if (optional.isPresent()) {
            studyYear = optional.get();
        } else {
            throw new RuntimeException("Rocnik s id: " + id + " nebola najdena!");
        }
        return studyYear;
    }

    @Override
    public StudyYear findByName(String name) {
        return studyYearRepository.findByName(name);
    }


    @Override
    public StudyYear save(StudyYear studyYear) {
        return studyYearRepository.save(studyYear);
    }

    @Override
    public void delete(int id) {
        delete(findOne(id));
    }

    @Override
    public void delete(StudyYear studyYear) {
        studyYearRepository.delete(studyYear);
    }
}
