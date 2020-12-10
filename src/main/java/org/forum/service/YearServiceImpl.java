package org.forum.service;

import org.forum.entities.StudyYear;
import org.forum.entities.Year;
import org.forum.repository.YearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YearServiceImpl implements YearService {

    @Autowired
    private YearRepository yearRepository;

    @Override
    public List<Year> findAll() {
        return yearRepository.findAll();
    }

    @Override
    public Year findOne(int id) {
        Optional<Year> optional = yearRepository.findById(id);
        Year year = null;
        if (optional.isPresent()) {
            year = optional.get();
        } else {
            throw new RuntimeException("Rok s id: " + id + " nebol najdeny!");
        }
        return year;
    }

    @Override
    public Year findByName(String name) {
        return yearRepository.findByName(name);
    }

    @Override
    public Year save(Year year) {
        return yearRepository.save(year);
    }

    @Override
    public void delete(int id) {
        delete(findOne(id));
    }

    @Override
    public void delete(Year year) {
        yearRepository.delete(year);
    }
}
