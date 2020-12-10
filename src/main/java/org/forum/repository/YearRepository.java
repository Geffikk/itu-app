package org.forum.repository;

import org.forum.entities.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.transform.sax.SAXTransformerFactory;

@Repository
public interface YearRepository extends JpaRepository<Year, Integer> {

    Year findByName(String name);

}
