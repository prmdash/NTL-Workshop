package softuni.workshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.workshop.data.entity.Project;

import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Set<Project> findAllByFinishedTrueOrderByNameDesc();

    Project findByName(String name);
}
