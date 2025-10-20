package org.example.projectface.service;

import org.example.projectface.entity.Student;
import org.example.projectface.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
  private StudentRepository studentRepository = new StudentRepository();

  public Student addStudent(Student student){
      return studentRepository.saveAll(student);
  }
}
