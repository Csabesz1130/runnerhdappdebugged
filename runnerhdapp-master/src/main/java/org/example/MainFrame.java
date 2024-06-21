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
import java.util.List;

public class MainFrame extends JFrame {
    private CompanyController companyController;
    private AuthController authController;
    private DashboardController dashboardController;
    private NotificationsController notificationsController;
    private UserManagementController userManagementController;
    private ReportsController reportsController;
    private SettingsController settingsController;
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
            FileInputStream serviceAccount = new FileInputStream("runnerhdapp-master/src/main/resources/runnerapp-232cc-firebase-adminsdk-2csiq-a10838e3ed.json");
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
        mainView = new MainView(authController, this, companyController);
        companyDetailsView = new CompanyDetailsView(companyController, this);

        mainPanel.add(loginView, "Login");
        mainPanel.add(mainView, "MainView");
        mainPanel.add(companyDetailsView, "CompanyDetailsView");
        mainPanel.add(new DashboardView(dashboardController), "Dashboard");
        mainPanel.add(new NotificationsView(notificationsController), "Notifications");
        mainPanel.add(new ReportsView(reportsController), "Reports");
        mainPanel.add(new SettingsView(settingsController), "Settings");
        mainPanel.add(new TaskView(companyController, new Company()), "Tasks");
        mainPanel.add(new UserManagementView(userManagementController), "UserManagement");

        add(mainPanel, BorderLayout.CENTER);
    }

    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    public void showMainView() {
        showView("MainView");
        mainView.loadFestivals();
        initMenuBar();
    }

    public void showCompanyDetails(Company company) {
        companyDetailsView.setCompany(company);
        showView("CompanyDetailsView");
    }

    public void refreshMainView() {
        mainView.refresh();
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem dataMenuItem = new JMenuItem("Data");
        dataMenuItem.addActionListener(e -> showView("MainView"));
        menu.add(dataMenuItem);

        JMenuItem dashboardMenuItem = new JMenuItem("Dashboard");
        dashboardMenuItem.addActionListener(e -> showView("Dashboard"));
        menu.add(dashboardMenuItem);

        JMenuItem notificationsMenuItem = new JMenuItem("Notifications");
        notificationsMenuItem.addActionListener(e -> showView("Notifications"));
        menu.add(notificationsMenuItem);

        JMenuItem reportsMenuItem = new JMenuItem("Reports");
        reportsMenuItem.addActionListener(e -> showView("Reports"));
        menu.add(reportsMenuItem);

        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(e -> showView("Settings"));
        menu.add(settingsMenuItem);

        JMenuItem tasksMenuItem = new JMenuItem("Tasks");
        tasksMenuItem.addActionListener(e -> showView("Tasks"));
        menu.add(tasksMenuItem);

        JMenuItem userManagementMenuItem = new JMenuItem("User Management");
        userManagementMenuItem.addActionListener(e -> showView("UserManagement"));
        menu.add(userManagementMenuItem);

        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(e -> logout());
        menu.add(logoutMenuItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
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