package org.example.views;

import org.example.MainFrame;
import org.example.controllers.AuthController;
import org.example.controllers.CompanyController;
import org.example.models.Company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainView extends JPanel {
    private final AuthController authController;
    private final MainFrame mainFrame;
    private final CompanyController companyController;

    private JComboBox<String> festivalComboBox;
    private JRadioButton installationRadioButton;
    private JRadioButton demolitionRadioButton;
    private JTable companyTable;
    private DefaultTableModel companyTableModel;
    private JButton logoutButton;

    public MainView(AuthController authController, MainFrame mainFrame, CompanyController companyController) {
        this.authController = authController;
        this.mainFrame = mainFrame;
        this.companyController = companyController;
        initComponents();
        loadFestivals();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel festivalLabel = new JLabel("Festival:");
        topPanel.add(festivalLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        festivalComboBox = new JComboBox<>();
        festivalComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchCompanies();
            }
        });
        topPanel.add(festivalComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        installationRadioButton = new JRadioButton("Company_Install");
        installationRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchCompanies();
            }
        });
        topPanel.add(installationRadioButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        demolitionRadioButton = new JRadioButton("Company_Demolition");
        demolitionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchCompanies();
            }
        });
        topPanel.add(demolitionRadioButton, gbc);

        ButtonGroup choiceButtonGroup = new ButtonGroup();
        choiceButtonGroup.add(installationRadioButton);
        choiceButtonGroup.add(demolitionRadioButton);

        add(topPanel, BorderLayout.NORTH);

        companyTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Last Modified", "Program"}, 0);
        companyTable = new JTable(companyTableModel);
        JScrollPane companyScrollPane = new JScrollPane(companyTable);
        add(companyScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        bottomPanel.add(logoutButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadFestivals() {
        List<String> festivals = companyController.getFestivals();
        festivalComboBox.removeAllItems();
        for (String festival : festivals) {
            festivalComboBox.addItem(festival);
        }
        if (!festivals.isEmpty()) {
            fetchCompanies();
        }
    }

    public void setTasks(List<Company> companies) {
        companyTableModel.setRowCount(0);
        for (Company company : companies) {
            Object[] rowData = {company.getId(), company.getCompanyName(), company.getLastModified(), company.getProgramName()};
            companyTableModel.addRow(rowData);
        }
    }


    private void fetchCompanies() {
        String selectedFestival = (String) festivalComboBox.getSelectedItem();
        String collectionName = installationRadioButton.isSelected() ? "Company_Install" : "Company_Demolition";

        companyController.getCompaniesByFestival(
                collectionName,
                selectedFestival,
                this::setTasks,  // Success callback
                error -> {
                    JOptionPane.showMessageDialog(this,
                            "Error fetching companies: " + error.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }  // Error callback
        );
    }

    private void updateCompanyTable(List<Company> companies) {
        companyTableModel.setRowCount(0);
        for (Company company : companies) {
            Object[] rowData = {company.getId(), company.getCompanyName(), company.getLastModified(), company.getProgramName()};
            companyTableModel.addRow(rowData);
        }
    }

    private void logout() {
        authController.logout();
        mainFrame.showLoginView();
    }
}