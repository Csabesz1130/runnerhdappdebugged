package org.example.views;

import org.example.MainFrame;
import org.example.controllers.AuthController;
import org.example.services.FirestoreService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class LoginView extends JPanel {

    public static final String[] FELHASZNÁLÓNEVEK = {"borisz", "trisztanv", "davidartur"};
    public static final String JELSZÓ = "1234";

    private AuthController authController;

    private JComboBox<String> felhasználónévComboBox;  // Use JComboBox for usernames
    private JPasswordField jelszóPasswordField;
    private JButton bejelentkezésButton;

    public LoginView(AuthController authController, MainFrame mainFrame) {
        // Initialize Firestore
        Firestore firestore = FirestoreClient.getFirestore();

        // Pass the Firestore instance to FirestoreService
        FirestoreService firestoreService = new FirestoreService(firestore);
        this.authController = new AuthController(firestoreService, FELHASZNÁLÓNEVEK, JELSZÓ);

        felhasználónévComboBox = new JComboBox<>(FELHASZNÁLÓNEVEK);  // Initialize JComboBox
        jelszóPasswordField = new JPasswordField(20);
        bejelentkezésButton = new JButton("Bejelentkezés");

        bejelentkezésButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String felhasználónév = (String) felhasználónévComboBox.getSelectedItem();
                String jelszó = new String(jelszóPasswordField.getPassword());

                if (LoginView.this.authController.login(felhasználónév, jelszó)) {
                    JOptionPane.showMessageDialog(null, "Sikeres bejelentkezés!");
                    mainFrame.showMainView(); // Call showMainView() after successful login
                } else {
                    JOptionPane.showMessageDialog(null, "Hibás felhasználónév vagy jelszó!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Felhasználónév:"), constraints);

        constraints.gridx = 1;
        add(felhasználónévComboBox, constraints); // Add JComboBox instead of JTextField

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel("Jelszó:"), constraints);

        constraints.gridx = 1;
        add(jelszóPasswordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(bejelentkezésButton, constraints);
    }
}