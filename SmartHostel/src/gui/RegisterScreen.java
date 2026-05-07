package gui;

import service.RegisterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterScreen extends JFrame {

    private JTextField     cmsField;
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JLabel         statusLabel;

    private RegisterService registerService = new RegisterService();
    private LoginScreen     parentLogin;

    public RegisterScreen(LoginScreen parentLogin) {
        this.parentLogin = parentLogin;

        setTitle("Smart Hostel - Student Registration");
        setSize(380, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // When this window closes, bring back the login screen
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                if (parentLogin != null) parentLogin.setVisible(true);
            }
        });

        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("CMS ID:"));
        cmsField = new JTextField();
        panel.add(cmsField);

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Confirm Password:"));
        confirmField = new JPasswordField();
        confirmField.addActionListener(e -> handleRegister()); // Enter key submits
        panel.add(confirmField);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        panel.add(new JLabel(""));
        panel.add(statusLabel);

        JButton registerBtn = new JButton("Create Account");
        registerBtn.addActionListener(e -> handleRegister());
        panel.add(new JLabel(""));
        panel.add(registerBtn);

        JButton backBtn = new JButton("Back to Login");
        backBtn.addActionListener(e -> {
            if (parentLogin != null) parentLogin.setVisible(true);
            dispose();
        });
        panel.add(new JLabel(""));
        panel.add(backBtn);

        add(panel);
    }

    private void handleRegister() {
        String cms      = cmsField.getText().trim();
        String username = usernameField.getText().trim();
        String pass     = new String(passwordField.getPassword()).trim();
        String confirm  = new String(confirmField.getPassword()).trim();

        // Basic validation
        if (cms.isEmpty() || username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }
        if (username.length() < 3) {
            statusLabel.setText("Username must be at least 3 characters.");
            return;
        }
        if (pass.length() < 4) {
            statusLabel.setText("Password must be at least 4 characters.");
            return;
        }
        if (!pass.equals(confirm)) {
            statusLabel.setText("Passwords do not match!");
            confirmField.setText("");
            return;
        }

        int result = registerService.register(cms, username, pass);

        switch (result) {
            case RegisterService.SUCCESS:
                JOptionPane.showMessageDialog(this,
                    "Registration successful!\nYou can now login with username: " + username);
                if (parentLogin != null) parentLogin.setVisible(true);
                dispose();
                break;

            case RegisterService.CMS_NOT_FOUND:
                statusLabel.setText("CMS ID not found. Contact admin first.");
                break;

            case RegisterService.ALREADY_LINKED:
                JOptionPane.showMessageDialog(this,
                    "This CMS ID is already registered.\nPlease go to Login.");
                break;

            case RegisterService.USERNAME_TAKEN:
                statusLabel.setText("Username already taken. Choose another.");
                usernameField.setText("");
                usernameField.requestFocus();
                break;

            default:
                statusLabel.setText("Database error. Please try again.");
        }
    }
}
