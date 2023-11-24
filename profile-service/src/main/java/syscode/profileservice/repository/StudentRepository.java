package syscode.profileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syscode.profileservice.entity.Student;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

}
