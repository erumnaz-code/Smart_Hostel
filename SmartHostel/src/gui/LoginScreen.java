package gui;

import model.User;
import service.LoginService;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         statusLabel;

    private LoginService loginService = new LoginService();

    public LoginScreen() {
        setTitle("Smart Hostel - Login");
        setSize(350, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.addActionListener(e -> handleLogin()); // Enter key triggers login
        panel.add(passwordField);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        panel.add(new JLabel(""));
        panel.add(statusLabel);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(new JLabel(""));
        panel.add(loginBtn);

        JButton registerBtn = new JButton("Register (New Student)");
        registerBtn.addActionListener(e -> openRegisterScreen());
        panel.add(new JLabel(""));
        panel.add(registerBtn);

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Enter both username and password.");
            return;
        }

        User user = loginService.login(username, password);

        if (user == null) {
            statusLabel.setText("Invalid username or password.");
            passwordField.setText("");
        } else {
            setVisible(false); 
            openDashboard(user);
        }
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "ADMIN":         new AdminDashboard(user).setVisible(true);  break;
            case "MESS_INCHARGE": new MessDashboard(user).setVisible(true);   break;
            case "STUDENT":       new StudentDashboard(user).setVisible(true); break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown role: " + user.getRole());
                setVisible(true);
        }
    }

    private void openRegisterScreen() {
        setVisible(false);
        new RegisterScreen(this).setVisible(true);
    }
}
