package softuni.workshop.service;

import softuni.workshop.data.entity.Project;
import softuni.workshop.data.view.ProjectViewModel;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface ProjectService {

    void importProjects() throws JAXBException;

    boolean areImported();

    String readProjectsXmlFile() throws IOException;

    String exportFinishedProjects();

    Project findByName(String name);

    List<ProjectViewModel> findAllFinishedProjects();
}
