package gui;

import model.Complaint;
import model.Student;
import model.User;
import service.ComplaintService;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private User loggedInUser;
    private StudentService   studentService   = new StudentService();
    private ComplaintService complaintService = new ComplaintService();

    // Student tab fields
    private JTextField cmsField, nameField, roomField, deptField, contactField;
    private JTable            studentTable;
    private DefaultTableModel studentTableModel;

    // Complaint tab
    private JTable            complaintTable;
    private DefaultTableModel complaintTableModel;

    public AdminDashboard(User user) {
        this.loggedInUser = user;

        setTitle("Admin Dashboard - " + user.getUsername());
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        loadStudents();
        loadComplaints();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

       
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        topBar.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginScreen().setVisible(true); });
        topBar.add(logoutBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);


        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Students", buildStudentPanel());
        tabs.addTab("View Complaints", buildComplaintsPanel());
        add(tabs, BorderLayout.CENTER);
    }

    // -------------------------------------------------------
    // STUDENT PANEL
    // -------------------------------------------------------
    private JPanel buildStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form at the top
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 6, 6));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        cmsField     = new JTextField(); cmsField.setToolTipText("CMS ID");
        nameField    = new JTextField(); nameField.setToolTipText("Full Name");
        roomField    = new JTextField(); roomField.setToolTipText("Room Number");
        deptField    = new JTextField(); deptField.setToolTipText("Department");
        contactField = new JTextField(); contactField.setToolTipText("Contact Number");

        // Row 1: Labels
        formPanel.add(new JLabel("CMS ID:"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Room No:"));
        formPanel.add(new JLabel("Department:"));
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(new JLabel("Actions:"));

        // Row 2: Fields + Buttons
        formPanel.add(cmsField);
        formPanel.add(nameField);
        formPanel.add(roomField);
        formPanel.add(deptField);
        formPanel.add(contactField);

        // Add/Update/Delete buttons in one small panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 4, 0));
        JButton addBtn    = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        addBtn.addActionListener(e    -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        formPanel.add(btnPanel);

        panel.add(formPanel, BorderLayout.NORTH);

        // Table in center
        String[] columns = {"ID", "CMS", "Name", "Room", "Department", "Contact"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);

        // Clicking a row fills the form fields
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                cmsField.setText((String) studentTableModel.getValueAt(row, 1));
                nameField.setText((String) studentTableModel.getValueAt(row, 2));
                roomField.setText((String) studentTableModel.getValueAt(row, 3));
                deptField.setText((String) studentTableModel.getValueAt(row, 4));
                contactField.setText((String) studentTableModel.getValueAt(row, 5));
            }
        });

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return panel;
    }

    private void addStudent() {
        if (cmsField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()
                || roomField.getText().trim().isEmpty() || deptField.getText().trim().isEmpty()
                || contactField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }
        Student s = new Student(0, cmsField.getText().trim(), nameField.getText().trim(),
                                roomField.getText().trim(), deptField.getText().trim(),
                                contactField.getText().trim());
        if (studentService.addStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearStudentForm();
            loadStudents();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student.");
        }
    }

    private void updateStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first!"); return; }
        int id = (int) studentTableModel.getValueAt(row, 0);
        Student s = new Student(id, cmsField.getText().trim(), nameField.getText().trim(),
                                roomField.getText().trim(), deptField.getText().trim(),
                                contactField.getText().trim());
        if (studentService.updateStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student updated!");
            loadStudents();
        }
    }

    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first!"); return; }
        int id = (int) studentTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (studentService.deleteStudent(id)) {
                JOptionPane.showMessageDialog(this, "Student deleted.");
                clearStudentForm();
                loadStudents();
            }
        }
    }

    private void loadStudents() {
        studentTableModel.setRowCount(0);
        for (Student s : studentService.getAllStudents()) {
            studentTableModel.addRow(new Object[]{
                s.getStudentId(), s.getCms(), s.getName(),
                s.getRoomNo(), s.getDepartment(), s.getContact()
            });
        }
    }

    private void clearStudentForm() {
        cmsField.setText(""); nameField.setText(""); roomField.setText("");
        deptField.setText(""); contactField.setText("");
        studentTable.clearSelection();
    }

    // -------------------------------------------------------
    // COMPLAINTS PANEL
    // -------------------------------------------------------
    private JPanel buildComplaintsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"#", "CMS ID", "Student Name", "Complaint", "Status", "Date"};
        complaintTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        complaintTable = new JTable(complaintTableModel);
        complaintTable.setRowHeight(25);
        complaintTable.getColumnModel().getColumn(3).setPreferredWidth(300); // wider complaint column

        panel.add(new JScrollPane(complaintTable), BorderLayout.CENTER);

        // Buttons at the bottom
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton resolveBtn = new JButton("Mark as Resolved");
        JButton refreshBtn = new JButton("Refresh");
        resolveBtn.addActionListener(e -> resolveComplaint());
        refreshBtn.addActionListener(e -> loadComplaints());
        btnRow.add(resolveBtn);
        btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);

        return panel;
    }

    private void resolveComplaint() {
        int row = complaintTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a complaint first!");
            return;
        }
        // Get the real DB id from the service (table shows serial numbers, not DB ids)
        List<Complaint> list = complaintService.getAllComplaints();
        int realId = list.get(row).getId();

        if (complaintService.resolveComplaint(realId)) {
            JOptionPane.showMessageDialog(this, "Complaint marked as resolved!");
            loadComplaints();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to resolve complaint.");
        }
    }

    private void loadComplaints() {
        complaintTableModel.setRowCount(0);
        int serial = 1;
        for (Complaint c : complaintService.getAllComplaints()) {
            complaintTableModel.addRow(new Object[]{
                serial++, c.getCms(), c.getName(), c.getText(), c.getStatus(), c.getCreatedAt()
            });
        }
    }
}
