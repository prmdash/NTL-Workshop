package softuni.workshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.workshop.data.entity.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Role findByAuthority(String role);
}
