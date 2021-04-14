package softuni.workshop.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.workshop.service.EmployeeService;
import softuni.workshop.service.ProjectService;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    @Autowired
    public ExportController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }


    @GetMapping("/finished-projects")
    public ModelAndView finishedProjects() {
        ModelAndView modelAndView = new ModelAndView("/export/export-finished-projects.html");
        String finishedProjects = this.projectService.exportFinishedProjects();
        modelAndView.addObject("finishedProjects", finishedProjects);
        return modelAndView;
    }

    @GetMapping("/older-employees")
    public ModelAndView olderEmployees() {
        ModelAndView modelAndView = new ModelAndView("/export/export-older-employees.html");
        String olderEmployees = this.employeeService.exportOlderEmployees();
        modelAndView.addObject("olderEmployees", olderEmployees);
        return modelAndView;
    }
}
