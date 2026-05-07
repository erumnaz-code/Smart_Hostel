package service;

import dao.AttendanceDAO;
import model.Attendance;

import java.util.List;

public class AttendanceService {

    private AttendanceDAO attendanceDAO = new AttendanceDAO();


    public boolean markAttendance(int studentId, String date, String mealType, String status, double price) {
        return attendanceDAO.markAttendance(studentId, date, mealType, status, price);
    }

    public List<Attendance> getStudentAttendance(int studentId) {
        return attendanceDAO.getAttendanceByStudent(studentId);
    }

    public int countPresentMeals(int studentId, String yearMonth) {
        return attendanceDAO.countPresent(studentId, yearMonth);
    }
}
