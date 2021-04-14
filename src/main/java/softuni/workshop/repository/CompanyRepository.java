package softuni.workshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.workshop.data.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByName(String name);

}
