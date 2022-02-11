package mark.service;

import mark.entity.Student;
import mark.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllUsers()
    {
        List<Student>studentRecords = new ArrayList<>();

        studentRepository.findAll().forEach(studentRecords::add);
        return studentRecords;
    }
    public void addStudent(Student studentRecord)
    {
        studentRepository.save(studentRecord);
    }
    public void deleteStudent(Student studentRecord)
    {
        studentRepository.delete(studentRecord);
    }
}
