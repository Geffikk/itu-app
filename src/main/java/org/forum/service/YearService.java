package org.forum.service;

import org.forum.entities.Year;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface YearService {

    List<Year> findAll();

    Year findOne(int id);

    Year findByName(String name);

    Year save(Year year);

    void delete(int id);

    void delete(Year year);
}
