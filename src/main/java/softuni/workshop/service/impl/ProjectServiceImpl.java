package softuni.workshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.workshop.data.dto.ProjectDto;
import softuni.workshop.data.dto.ProjectRootDto;
import softuni.workshop.data.entity.Company;
import softuni.workshop.data.entity.Project;
import softuni.workshop.data.view.ProjectViewModel;
import softuni.workshop.repository.ProjectRepository;
import softuni.workshop.service.CompanyService;
import softuni.workshop.service.ProjectService;
import softuni.workshop.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.workshop.constants.PathConstants.PROJECTS_PATH;


@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final CompanyService companyService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, XmlParser xmlParser, ModelMapper modelMapper, CompanyService companyService) {
        this.projectRepository = projectRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.companyService = companyService;
    }

    @Override
    public void importProjects() throws JAXBException {
        ProjectRootDto projectRootDto = this.xmlParser.parseXml(ProjectRootDto.class, PROJECTS_PATH);

        for (ProjectDto projectDto : projectRootDto.getProjectDtoList()) {
            Project project = this.modelMapper.map(projectDto, Project.class);
            project.setCompany(this.companyService.findByName(projectDto.getCompany().getName()));
            this.projectRepository.saveAndFlush(project);
        }
    }

    @Override
    public boolean areImported() {
        return this.projectRepository.count() > 0;
    }

    @Override
    public String readProjectsXmlFile() throws IOException {
        return String.join("\n", Files.readAllLines(Path.of(PROJECTS_PATH)));
    }

    @Override
    public String exportFinishedProjects() {
        StringBuilder stringBuilder = new StringBuilder();
        List<ProjectViewModel> projects = findAllFinishedProjects();

        for (ProjectViewModel project : projects) {
            stringBuilder.append(String.format("Project Name: %s", project.getName()))
                    .append(System.lineSeparator())
                    .append(String.format("  Description: %s", project.getDescription()))
                    .append(System.lineSeparator())
                    .append(String.format("  Salary: %f", project.getPayment()))
                    .append(System.lineSeparator());

        }

        return stringBuilder.toString().trim();
    }

    @Override
    public Project findByName(String name) {
        return
                this.projectRepository.findByName(name);
    }

    @Override
    public List<ProjectViewModel> findAllFinishedProjects() {
        return this.projectRepository
                .findAllByFinishedTrueOrderByNameDesc()
                .stream()
                .map(p -> this.modelMapper.map(p, ProjectViewModel.class))
                .collect(Collectors.toList());
    }
}
