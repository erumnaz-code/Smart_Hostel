package gui;

import model.Attendance;
import model.Billing;
import model.Complaint;
import model.Menu;
import model.Student;
import model.User;
import service.AttendanceService;
import service.BillingService;
import service.ComplaintService;
import service.MenuService;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentDashboard extends JFrame {

    private User    loggedInUser;
    private Student student;

    private StudentService    studentService    = new StudentService();
    private AttendanceService attendanceService = new AttendanceService();
    private BillingService    billingService    = new BillingService();
    private ComplaintService  complaintService  = new ComplaintService();
    private MenuService       menuService       = new MenuService();

    private String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private JTable            attendanceTable;
    private DefaultTableModel attendanceModel;
    private JTable            billTable;
    private DefaultTableModel billModel;
    private JTable            complaintTable;
    private DefaultTableModel complaintModel;
    private JTextArea         complaintInput;

    public StudentDashboard(User user) {
        this.loggedInUser = user;
        this.student      = studentService.getStudentByUserId(user.getId());

        setTitle("Student Dashboard - " + user.getUsername());
        setSize(920, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // -- Top bar --
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String name = (student != null) ? student.getName() : loggedInUser.getUsername();
        JLabel titleLabel = new JLabel("Welcome, " + name);
        topBar.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginScreen().setVisible(true); });
        topBar.add(logoutBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // -- Tabs --
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Profile",   buildProfilePanel());
        tabs.addTab("Attendance",   buildAttendancePanel());
        tabs.addTab("My Bills",     buildBillsPanel());
        tabs.addTab("Complaints",   buildComplaintsPanel());
        tabs.addTab("Today's Menu", buildMenuPanel());
        add(tabs, BorderLayout.CENTER);
    }

    // -------------------------------------------------------
    // PROFILE PANEL
    // -------------------------------------------------------
    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.anchor  = GridBagConstraints.WEST;

        // Heading
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel heading = new JLabel("My Profile");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(heading, gbc);
        gbc.gridwidth = 1;

        if (student == null) {
            gbc.gridy = 1;
            panel.add(new JLabel("Profile not linked. Ask admin to link your account."), gbc);
            return panel;
        }

        // Profile rows: label + value
        String[][] info = {
            {"CMS ID:",     student.getCms()},
            {"Full Name:",  student.getName()},
            {"Room No:",    student.getRoomNo()},
            {"Department:", student.getDepartment()},
            {"Contact:",    student.getContact()},
            {"Username:",   loggedInUser.getUsername()},
            {"Role:",       loggedInUser.getRole()}
        };

        for (int i = 0; i < info.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            JLabel lbl = new JLabel(info[i][0]);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            panel.add(new JLabel(info[i][1]), gbc);
        }

        return panel;
    }

    // -------------------------------------------------------
    // ATTENDANCE PANEL
    // -------------------------------------------------------
    private JPanel buildAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadBtn = new JButton("Refresh Attendance");
        loadBtn.addActionListener(e -> loadMyAttendance());
        top.add(loadBtn);
        panel.add(top, BorderLayout.NORTH);

        String[] cols = {"CMS ID", "Name", "Date", "Meal", "Status", "Price (PKR)"};
        attendanceModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        attendanceTable = new JTable(attendanceModel);
        attendanceTable.setRowHeight(25);
        panel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        loadMyAttendance();
        return panel;
    }

    private void loadMyAttendance() {
        attendanceModel.setRowCount(0);
        if (student == null) return;
        for (Attendance a : attendanceService.getStudentAttendance(student.getStudentId())) {
            attendanceModel.addRow(new Object[]{
                a.getCms(), a.getStudentName(),
                a.getDate(), a.getMealType(),
                a.getStatus(), String.format("%.2f", a.getMealPrice())
            });
        }
    }

    // -------------------------------------------------------
    // BILLS PANEL
    // -------------------------------------------------------
    private JPanel buildBillsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadBtn = new JButton("Refresh Bills");
        loadBtn.addActionListener(e -> loadMyBills());
        top.add(loadBtn);
        panel.add(top, BorderLayout.NORTH);

        String[] cols = {"#", "Month", "Amount (PKR)", "Status"};
        billModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        billTable = new JTable(billModel);
        billTable.setRowHeight(25);
        panel.add(new JScrollPane(billTable), BorderLayout.CENTER);

        loadMyBills();
        return panel;
    }

    private void loadMyBills() {
        billModel.setRowCount(0);
        if (student == null) return;
        int serial = 1;
        for (Billing b : billingService.getStudentBills(student.getStudentId())) {
            billModel.addRow(new Object[]{
                serial++, b.getMonth(),
                String.format("%.2f", b.getTotalAmount()), b.getStatus()
            });
        }
    }

    // -------------------------------------------------------
    // COMPLAINTS PANEL
    // -------------------------------------------------------
    private JPanel buildComplaintsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Submit area at top
        JPanel submitPanel = new JPanel(new BorderLayout(5, 5));
        submitPanel.setBorder(BorderFactory.createTitledBorder("Submit New Complaint"));

        complaintInput = new JTextArea(4, 40);
        complaintInput.setLineWrap(true);
        complaintInput.setWrapStyleWord(true);
        submitPanel.add(new JScrollPane(complaintInput), BorderLayout.CENTER);

        JPanel submitBtnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit Complaint");
        submitBtn.addActionListener(e -> submitComplaint());
        submitBtnRow.add(submitBtn);
        submitPanel.add(submitBtnRow, BorderLayout.SOUTH);
        panel.add(submitPanel, BorderLayout.NORTH);

        // Past complaints table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("My Complaints"));

        String[] cols = {"#", "Complaint", "Status", "Date"};
        complaintModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        complaintTable = new JTable(complaintModel);
        complaintTable.setRowHeight(25);
        complaintTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablePanel.add(new JScrollPane(complaintTable), BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);

        loadMyComplaints();
        return panel;
    }

    private void submitComplaint() {
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Profile not linked. Contact admin."); return;
        }
        String text = complaintInput.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please write your complaint first!"); return;
        }
        if (complaintService.submitComplaint(student.getStudentId(), text)) {
            JOptionPane.showMessageDialog(this, "Complaint submitted successfully!");
            complaintInput.setText("");
            loadMyComplaints();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit complaint.");
        }
    }

    private void loadMyComplaints() {
        complaintModel.setRowCount(0);
        if (student == null) return;
        int serial = 1;
        for (Complaint c : complaintService.getStudentComplaints(student.getStudentId())) {
            complaintModel.addRow(new Object[]{
                serial++, c.getText(), c.getStatus(), c.getCreatedAt()
            });
        }
    }

    // -------------------------------------------------------
    // MENU PANEL
    // -------------------------------------------------------
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("Today's Menu — " + today);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        Menu menu = menuService.getMenuByDate(today);

        JPanel menuCard = new JPanel(new GridLayout(4, 2, 10, 10));
        menuCard.setBorder(BorderFactory.createTitledBorder("Menu"));

        if (menu != null) {
            menuCard.add(new JLabel("Breakfast:"));
            menuCard.add(new JLabel(menu.getBreakfast() + "  —  PKR " + menu.getBreakfastPrice()));
            menuCard.add(new JLabel("Lunch:"));
            menuCard.add(new JLabel(menu.getLunch() + "  —  PKR " + menu.getLunchPrice()));
            menuCard.add(new JLabel("Dinner:"));
            menuCard.add(new JLabel(menu.getDinner() + "  —  PKR " + menu.getDinnerPrice()));
            menuCard.add(new JLabel("Total (all meals):"));
            menuCard.add(new JLabel("PKR " + (menu.getBreakfastPrice() + menu.getLunchPrice() + menu.getDinnerPrice())));
        } else {
            menuCard.add(new JLabel("No menu set for today. Please check back later."));
        }

        panel.add(menuCard, BorderLayout.CENTER);
        return panel;
    }
}
