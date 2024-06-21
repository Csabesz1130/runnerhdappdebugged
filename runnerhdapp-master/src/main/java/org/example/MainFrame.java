package org.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.example.controllers.*;
import org.example.models.Company;
import org.example.services.FirestoreService;
import org.example.views.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFrame extends JFrame {
    private CompanyController companyController;
    private AuthController authController;
    private LoginView loginView;
    private MainView mainView;
    private CompanyDetailsView companyDetailsView;
    private JMenuBar menuBar;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        Firestore db = initializeFirestore();
        FirestoreService firestoreService = new FirestoreService(db);
        this.authController = new AuthController(firestoreService, LoginView.FELHASZNÁLÓNEVEK, LoginView.JELSZÓ);
        this.companyController = new CompanyController(firestoreService);

        initUI();
    }

    private Firestore initializeFirestore() {
        try {
            FileInputStream serviceAccount = new FileInputStream("path/to/your/firebase-credentials.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            return FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initUI() {
        setTitle("RunnerHDApp");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginView = new LoginView(authController, this);
        mainView = new MainView(companyController, this);
        companyDetailsView = new CompanyDetailsView(companyController);

        mainPanel.add(loginView, "Login");
        mainPanel.add(mainView, "MainView");
        mainPanel.add(companyDetailsView, "CompanyDetailsView");

        add(mainPanel, BorderLayout.CENTER);
    }

    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    public void showMainView() {
        showView("MainView");
        mainView.refresh();
        initMenuBar();
    }

    public void showCompanyDetails(Company company) {
        companyDetailsView.setCompany(company);
        showView("CompanyDetailsView");
        System.out.println("Switched to CompanyDetailsView for company: " + company.getId());
    }

    public void closeCompanyDetails() {
        showView("MainView");
        System.out.println("Switched back to MainView");
    }

    private void initMenuBar() {
        // Implement menu bar initialization
    }

    private void logout() {
        authController.logout();
        setJMenuBar(null);
        showView("Login");
    }

    public void showLoginView() {
        showView("Login");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainFrame ex = new MainFrame();
            ex.setVisible(true);
        });
    }
}