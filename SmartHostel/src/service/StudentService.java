package service;

import dao.StudentDAO;
import model.Student;

import java.util.List;

public class StudentService {

    private StudentDAO studentDAO = new StudentDAO();

    public boolean addStudent(Student s) {
        // Could add validation here (e.g., CMS format check)
        if (s.getCms() == null || s.getCms().isEmpty()) {
            System.out.println("CMS ID cannot be empty!");
            return false;
        }
        return studentDAO.addStudent(s);
    }

    public boolean updateStudent(Student s) {
        return studentDAO.updateStudent(s);
    }

    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public Student getStudentByUserId(int userId) {
        return studentDAO.getStudentByUserId(userId);
    }
}
