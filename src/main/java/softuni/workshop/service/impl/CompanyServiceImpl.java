package softuni.workshop.service.impl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.workshop.data.entity.Company;
import softuni.workshop.repository.CompanyRepository;
import softuni.workshop.data.dto.CompanyDto;
import softuni.workshop.data.dto.CompanyRootDto;
import softuni.workshop.service.CompanyService;
import softuni.workshop.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.workshop.constants.PathConstants.COMPANIES_PATH;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, XmlParser xmlParser, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public void importCompanies() throws JAXBException {
        CompanyRootDto companyRootDto = this.xmlParser.parseXml(CompanyRootDto.class, COMPANIES_PATH);

        for (CompanyDto companyDto : companyRootDto.getCompanyDtoList()) {
            this.companyRepository.saveAndFlush(this.modelMapper.map(companyDto, Company.class));
        }
    }

    @Override
    public boolean areImported() {
        return this.companyRepository.count() > 0;
    }

    @Override
    public String readCompaniesXmlFile() throws IOException {
        return String.join("\n", Files.readAllLines(Path.of(COMPANIES_PATH)));
    }

    @Override
    public Company findByName(String name) {
        return this.companyRepository.findByName(name);
    }
}
