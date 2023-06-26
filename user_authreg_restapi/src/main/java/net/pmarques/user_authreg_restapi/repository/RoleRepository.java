package net.pmarques.user_authreg_restapi.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import net.pmarques.user_authreg_restapi.entity.Role;
import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long> {
Optional<Role> findByName(String name);
}