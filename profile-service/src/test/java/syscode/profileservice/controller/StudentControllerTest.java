package syscode.profileservice.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import syscode.profileservice.entity.Student;
import syscode.profileservice.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController studentController;

    final String testName1 = "Peter";
    final String testEmail1 = "peter@test.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListStudents() {
        Student testStudent1 = new Student();
        testStudent1.setId(UUID.randomUUID());
        testStudent1.setName(testName1);
        testStudent1.setEmail(testEmail1);

        List<Student> students = List.of(testStudent1);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentController.list();

        assertEquals(students, result);
    }

    @Test
    void testAddNewStudent() {
        Student testStudent1 = new Student();
        testStudent1.setName(testName1);
        testStudent1.setEmail(testEmail1);

        when(studentRepository.save(testStudent1)).thenAnswer(request -> {
            Student savedStudent = request.getArgument(0);
            savedStudent.setId(UUID.randomUUID());
            return savedStudent;
        });
        Student result = studentController.addNewStudent(testStudent1);

        assertNotNull(result.getId());
        assertEquals(testStudent1.getName(), result.getName());
        assertEquals(testStudent1.getEmail(), result.getEmail());
        verify(studentRepository, times(1)).save(testStudent1);
    }

    @Test
    void testModifyStudent() {
        UUID studentId = UUID.randomUUID();
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName(testName1);
        existingStudent.setEmail(testEmail1);

        Student modifiedStudent = new Student();
        modifiedStudent.setId(studentId);
        modifiedStudent.setName("Modified");
        modifiedStudent.setEmail("modified@test.com");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student result = studentController.modifyStudent(modifiedStudent, studentId);

        assertNotNull(result.getId());
        assertEquals(modifiedStudent.getName(), result.getName());
        assertEquals(modifiedStudent.getEmail(), result.getEmail());
        verify(studentRepository, times(1)).findById(eq(studentId));
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testModifyStudentEntityNotFound() {
        UUID studentId = UUID.randomUUID();
        Student modifiedStudent = new Student();
        modifiedStudent.setId(studentId);
        modifiedStudent.setName("Modified");
        modifiedStudent.setEmail("modified.com");
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentController.modifyStudent(modifiedStudent, studentId));
        verify(studentRepository, times(1)).findById(eq(studentId));
        verify(studentRepository, never()).save(modifiedStudent);
    }

    @Test
    void testDeleteStudent() {
        UUID studentId = UUID.randomUUID();

        studentController.deleteStudent(studentId);

        verify(studentRepository, times(1)).deleteById(eq(studentId));
    }

}