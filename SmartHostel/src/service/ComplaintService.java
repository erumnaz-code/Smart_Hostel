package service;

import dao.ComplaintDAO;
import model.Complaint;

import java.util.List;

public class ComplaintService {

    private ComplaintDAO complaintDAO = new ComplaintDAO();

    public boolean submitComplaint(int studentId, String text) {
        if (text == null || text.trim().isEmpty()) {
            System.out.println("Complaint text cannot be empty.");
            return false;
        }
        return complaintDAO.addComplaint(studentId, text.trim());
    }

    public boolean resolveComplaint(int complaintId) {
        return complaintDAO.updateStatus(complaintId, "RESOLVED");
    }

    public List<Complaint> getAllComplaints() {
        return complaintDAO.getAllComplaints();
    }

    public List<Complaint> getStudentComplaints(int studentId) {
        return complaintDAO.getComplaintsByStudent(studentId);
    }
}
