package gui;

import model.Attendance;
import model.Billing;
import model.Menu;
import model.Student;
import model.User;
import service.AttendanceService;
import service.BillingService;
import service.MenuService;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessDashboard extends JFrame {

    private User loggedInUser;
    private AttendanceService attendanceService = new AttendanceService();
    private MenuService       menuService       = new MenuService();
    private BillingService    billingService    = new BillingService();
    private StudentService    studentService    = new StudentService();

    private String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    // Attendance tab
    private JTable            attendanceTable;
    private DefaultTableModel attendanceModel;
    private JComboBox<String> mealTypeCombo;
    private JComboBox<String> statusCombo;
    private List<Student>     studentList;

    // Menu tab
    private JTextField menuDateField, breakfastField, lunchField, dinnerField;
    private JTextField bfPriceField, lunchPriceField, dinnerPriceField;

    // Billing tab
    private JTable            billTable;
    private DefaultTableModel billModel;
    private JTextField        monthField, yearMonthField;
    private JComboBox<String> billStudentCombo;
    private List<Student>     billStudentList;

    public MessDashboard(User user) {
        this.loggedInUser = user;

        setTitle("Mess Dashboard - " + user.getUsername());
        setSize(980, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // -- Top bar --
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Mess In-Charge Dashboard");
        topBar.add(titleLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> { dispose(); new LoginScreen().setVisible(true); });
        topBar.add(logoutBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // -- Tabs --
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Mark Attendance", buildAttendancePanel());
        tabs.addTab("Manage Menu",     buildMenuPanel());
        tabs.addTab("Generate Bill",   buildBillingPanel());
        add(tabs, BorderLayout.CENTER);
    }

    // -------------------------------------------------------
    // ATTENDANCE PANEL
    // -------------------------------------------------------
    private JPanel buildAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Controls at top
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        controls.setBorder(BorderFactory.createTitledBorder("Mark Attendance for: " + today));

        controls.add(new JLabel("Meal:"));
        mealTypeCombo = new JComboBox<>(new String[]{"BREAKFAST", "LUNCH", "DINNER"});
        controls.add(mealTypeCombo);

        controls.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(new String[]{"PRESENT", "ABSENT"});
        controls.add(statusCombo);

        JButton loadBtn    = new JButton("Load Students");
        JButton markBtn    = new JButton("Mark Selected");
        JButton markAllBtn = new JButton("Mark All Present");
        loadBtn.addActionListener(e    -> loadStudentsIntoAttendanceTable());
        markBtn.addActionListener(e    -> markSelectedAttendance());
        markAllBtn.addActionListener(e -> markAllPresent());
        controls.add(loadBtn);
        controls.add(markBtn);
        controls.add(markAllBtn);

        panel.add(controls, BorderLayout.NORTH);

        // Table
        String[] cols = {"CMS ID", "Name", "Room", "Status"};
        attendanceModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        attendanceTable = new JTable(attendanceModel);
        attendanceTable.setRowHeight(25);
        panel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        loadStudentsIntoAttendanceTable();
        return panel;
    }

    private void loadStudentsIntoAttendanceTable() {
        attendanceModel.setRowCount(0);
        studentList = studentService.getAllStudents();
        for (Student s : studentList) {
            attendanceModel.addRow(new Object[]{ s.getCms(), s.getName(), s.getRoomNo(), "-" });
        }
    }

    private void markSelectedAttendance() {
        int row = attendanceTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student row first!"); return; }
        if (studentList == null || row >= studentList.size()) return;

        int    studentId = studentList.get(row).getStudentId();
        String meal      = (String) mealTypeCombo.getSelectedItem();
        String status    = (String) statusCombo.getSelectedItem();
        double price     = getPriceForMeal(meal); // get price from today's menu

        if (attendanceService.markAttendance(studentId, today, meal, status, price)) {
            attendanceModel.setValueAt(status, row, 3);
            JOptionPane.showMessageDialog(this,
                "Attendance marked: " + status + " for " + meal
                + "\nStudent: " + studentList.get(row).getName()
                + "\nPrice recorded: PKR " + price);
        }
    }

    private void markAllPresent() {
        if (studentList == null || studentList.isEmpty()) return;
        String meal  = (String) mealTypeCombo.getSelectedItem();
        double price = getPriceForMeal(meal);

        if (price == 0.0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "No price set for today's " + meal + " in the menu.\n" +
                "Bill will record PKR 0. Continue anyway?",
                "No Price Found", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
        }

        for (int i = 0; i < studentList.size(); i++) {
            attendanceService.markAttendance(
                studentList.get(i).getStudentId(), today, meal, "PRESENT", price);
            attendanceModel.setValueAt("PRESENT", i, 3);
        }
        JOptionPane.showMessageDialog(this,
            "All students marked PRESENT for " + meal + "\nPrice per student: PKR " + price);
    }

    // Get meal price from today's menu
    private double getPriceForMeal(String mealType) {
        Menu menu = menuService.getMenuByDate(today);
        if (menu == null) return 0.0;
        return menu.getPriceForMeal(mealType);
    }

    // -------------------------------------------------------
    // MENU PANEL
    // -------------------------------------------------------
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Set Daily Menu"));

        menuDateField    = new JTextField(today);
        breakfastField   = new JTextField();
        lunchField       = new JTextField();
        dinnerField      = new JTextField();
        bfPriceField     = new JTextField("80");
        lunchPriceField  = new JTextField("100");
        dinnerPriceField = new JTextField("120");

        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(menuDateField);
        form.add(new JLabel("Breakfast:"));          form.add(breakfastField);
        form.add(new JLabel("Breakfast Price (PKR):")); form.add(bfPriceField);
        form.add(new JLabel("Lunch:"));              form.add(lunchField);
        form.add(new JLabel("Lunch Price (PKR):"));  form.add(lunchPriceField);
        form.add(new JLabel("Dinner:"));             form.add(dinnerField);
        form.add(new JLabel("Dinner Price (PKR):")); form.add(dinnerPriceField);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton saveBtn = new JButton("Save Menu");
        JButton loadBtn = new JButton("Load Today's Menu");
        saveBtn.addActionListener(e -> saveMenu());
        loadBtn.addActionListener(e -> loadTodayMenu());
        btnRow.add(saveBtn);
        btnRow.add(loadBtn);
        form.add(new JLabel(""));
        form.add(btnRow);

        panel.add(form, BorderLayout.NORTH);

        JLabel info = new JLabel(
            "<html><i style='color:gray'>Prices are automatically used when marking attendance.</i></html>");
        info.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        panel.add(info, BorderLayout.CENTER);

        return panel;
    }

    private void saveMenu() {
        try {
            String dateStr = menuDateField.getText().trim();
            String bf      = breakfastField.getText().trim();
            String lu      = lunchField.getText().trim();
            String di      = dinnerField.getText().trim();

            if (bf.isEmpty() || lu.isEmpty() || di.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all meal fields!"); return;
            }

            Menu menu = new Menu(
                0, dateStr, bf, lu, di,
                Double.parseDouble(bfPriceField.getText().trim()),
                Double.parseDouble(lunchPriceField.getText().trim()),
                Double.parseDouble(dinnerPriceField.getText().trim())
            );

            if (menuService.saveMenu(menu)) {
                JOptionPane.showMessageDialog(this,
                    "Menu saved for " + menu.getDate()
                    + "\nBreakfast: PKR " + menu.getBreakfastPrice()
                    + "  |  Lunch: PKR " + menu.getLunchPrice()
                    + "  |  Dinner: PKR " + menu.getDinnerPrice());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save menu.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prices must be numbers!");
        }
    }

    private void loadTodayMenu() {
        Menu menu = menuService.getMenuByDate(today);
        if (menu != null) {
            menuDateField.setText(menu.getDate());
            breakfastField.setText(menu.getBreakfast());
            lunchField.setText(menu.getLunch());
            dinnerField.setText(menu.getDinner());
            bfPriceField.setText(String.valueOf(menu.getBreakfastPrice()));
            lunchPriceField.setText(String.valueOf(menu.getLunchPrice()));
            dinnerPriceField.setText(String.valueOf(menu.getDinnerPrice()));
        } else {
            JOptionPane.showMessageDialog(this, "No menu found for today. Please create one.");
        }
    }

    // -------------------------------------------------------
    // BILLING PANEL
    // -------------------------------------------------------
    private JPanel buildBillingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Controls at top
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        controls.setBorder(BorderFactory.createTitledBorder("Generate Monthly Bill"));

        billStudentList = studentService.getAllStudents();
        String[] studentNames = new String[billStudentList.size()];
        for (int i = 0; i < billStudentList.size(); i++) {
            studentNames[i] = billStudentList.get(i).getName()
                            + " (" + billStudentList.get(i).getCms() + ")";
        }
        billStudentCombo = new JComboBox<>(studentNames);
        controls.add(new JLabel("Student:"));
        controls.add(billStudentCombo);

        // Default to current month
        String currentMonthLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
        String currentYearMonth  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        monthField     = new JTextField(currentMonthLabel, 10);
        yearMonthField = new JTextField(currentYearMonth, 8);

        controls.add(new JLabel("Month Label:")); controls.add(monthField);
        controls.add(new JLabel("YYYY-MM:"));     controls.add(yearMonthField);

        JButton generateBtn = new JButton("Generate Bill");
        JButton allBillsBtn = new JButton("Show All Bills");
        generateBtn.addActionListener(e -> generateBill());
        allBillsBtn.addActionListener(e -> loadAllBills());
        controls.add(generateBtn);
        controls.add(allBillsBtn);

        panel.add(controls, BorderLayout.NORTH);

        // Bill table
        String[] cols = {"#", "CMS ID", "Student Name", "Month", "Amount (PKR)", "Status"};
        billModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        billTable = new JTable(billModel);
        billTable.setRowHeight(25);
        panel.add(new JScrollPane(billTable), BorderLayout.CENTER);

        loadAllBills();
        return panel;
    }

    private void generateBill() {
        int idx = billStudentCombo.getSelectedIndex();
        if (idx < 0 || billStudentList == null || billStudentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students available!"); return;
        }
        int    studentId = billStudentList.get(idx).getStudentId();
        String month     = monthField.getText().trim();
        String yearMonth = yearMonthField.getText().trim();

        if (billingService.generateBill(studentId, month, yearMonth)) {
            double amount = billingService.calculateBill(studentId, yearMonth);
            JOptionPane.showMessageDialog(this,
                "Bill generated!\nStudent: " + billStudentList.get(idx).getName()
                + "\nMonth: " + month
                + "\nAmount: PKR " + String.format("%.2f", amount));
            loadAllBills();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to generate bill.");
        }
    }

    private void loadAllBills() {
        billModel.setRowCount(0);
        int serial = 1;
        for (Billing b : billingService.getAllBills()) {
            billModel.addRow(new Object[]{
                serial++, b.getCms(), b.getStudentName(),
                b.getMonth(), String.format("%.2f", b.getTotalAmount()), b.getStatus()
            });
        }
    }
}
