import gui.LoginScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities to start GUI on the Event Dispatch Thread (EDT)
                SwingUtilities.invokeLater(() -> {
    
            // Open the Login Screen
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);

            System.out.println("Smart Hostel Management System started!");
        });
	}
}