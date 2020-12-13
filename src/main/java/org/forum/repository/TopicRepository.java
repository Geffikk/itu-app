package org.forum.repository;

import org.forum.entities.Section;
import org.forum.entities.Topic;
import org.forum.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    Set<Topic> findBySection(Section section);

    Set<Topic> findByUser(User user);

    List<Topic> findAllByOrderByCreationDateDesc();

    List<Topic> findTop5ByOrderByCreationDateDesc();

    List<Topic> findByContentLike(String s);

    List<Topic> findByTitleLike(String s);
}
