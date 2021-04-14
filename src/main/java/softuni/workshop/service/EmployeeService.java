package softuni.workshop.service;

import softuni.workshop.data.view.EmployeeViewModel;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    void importEmployees() throws JAXBException;

    boolean areImported();

    String readEmployeesXmlFile() throws IOException;

    String exportOlderEmployees();

    List<EmployeeViewModel> findAllByAge();
}
