package softuni.workshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.workshop.data.dto.EmployeeDto;
import softuni.workshop.data.dto.EmployeeRootDto;
import softuni.workshop.data.entity.Employee;
import softuni.workshop.data.view.EmployeeViewModel;
import softuni.workshop.repository.EmployeeRepository;
import softuni.workshop.service.EmployeeService;
import softuni.workshop.service.ProjectService;
import softuni.workshop.util.XmlParser;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static softuni.workshop.constants.PathConstants.EMPLOYEES_PATH;
import static softuni.workshop.constants.PathConstants.PROJECTS_PATH;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ProjectService projectService;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, XmlParser xmlParser, ModelMapper modelMapper, ProjectService projectService) {
        this.employeeRepository = employeeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.projectService = projectService;
    }

    @Override
    public void importEmployees() throws JAXBException {
        EmployeeRootDto employeeRootDto = this.xmlParser.parseXml(EmployeeRootDto.class, EMPLOYEES_PATH);

        for (EmployeeDto employeeDto : employeeRootDto.getEmployeeDtoList()) {
            Employee employee = this.modelMapper.map(employeeDto, Employee.class);
            employee.setProject(
                    this.projectService.findByName(employeeDto.getProjectDto().getName())
            );

            this.employeeRepository.saveAndFlush(employee);
        }
    }

    @Override
    public boolean areImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return String.join("\n", Files.readAllLines(Path.of(PROJECTS_PATH)));
    }

    @Override
    public String exportOlderEmployees() {
        return findAllByAge()
                .stream()
                .map(EmployeeViewModel::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<EmployeeViewModel> findAllByAge() {
        return this.employeeRepository.findAllByAgeGreaterThanOrderByFirstName(25)
                .stream()
                .map(e -> this.modelMapper.map(e, EmployeeViewModel.class))
                .collect(Collectors.toList());
    }
}
