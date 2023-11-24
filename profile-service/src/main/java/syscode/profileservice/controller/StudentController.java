package syscode.profileservice.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import syscode.profileservice.entity.Student;
import syscode.profileservice.repository.StudentRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

    // private final AddressService addressService;
    private final StudentRepository studentRepository;

    @GetMapping
    List<Student> list() {
        log.info("Students list requested.");
        List<Student> studentList = studentRepository.findAll();
        //Did not know where I meant to use the address service, so I just put this here, If you wanted to try it
        // studentList.forEach(student -> {
        //     log.info("Getting address for student: " + student.getId());
        //     student.setAddress(addressService.getAddressData());
        // });
        return studentList;
    }

    @PostMapping
    Student addNewStudent(@RequestBody @Valid Student student) {
        log.info("Creating new student.");
        student.setId(UUID.randomUUID());
        return studentRepository.save(student);
    }

    @PutMapping("{id}")
    Student modifyStudent(@RequestBody @Valid Student modifiedStudent, @PathVariable UUID id) {
        log.info("Modifying student with id " + id);
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(modifiedStudent.getName());
                    student.setEmail(modifiedStudent.getEmail());
                    return studentRepository.save(student);
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("{id}")
    void deleteStudent(@PathVariable UUID id) {
        log.info("Deleting student with id " + id);
        studentRepository.deleteById(id);
    }

}


