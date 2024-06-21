package org.example.views;

import org.example.MainFrame;
import org.example.controllers.CompanyController;
import org.example.models.Company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainView extends JPanel {
    private final CompanyController companyController;
    private final MainFrame mainFrame;

    private JComboBox<String> festivalComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JTable companyTable;
    private DefaultTableModel companyTableModel;

    public MainView(CompanyController companyController, MainFrame mainFrame) {
        this.companyController = companyController;
        this.mainFrame = mainFrame;
        initComponents();
        loadFestivals();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        festivalComboBox = new JComboBox<>();
        festivalComboBox.addActionListener(e -> fetchCompanies());
        topPanel.add(new JLabel("Festival:"));
        topPanel.add(festivalComboBox);

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        companyTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Program"}, 0);
        companyTable = new JTable(companyTableModel);
        JScrollPane tableScrollPane = new JScrollPane(companyTable);
        add(tableScrollPane, BorderLayout.CENTER);

        companyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = companyTable.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    String companyId = (String) companyTableModel.getValueAt(row, 0);
                    openCompanyDetails(companyId);
                }
            }
        });
    }

    public void loadFestivals() {
        List<String> festivals = companyController.getFestivals();
        festivalComboBox.removeAllItems();
        for (String festival : festivals) {
            festivalComboBox.addItem(festival);
        }
        System.out.println("Loaded " + festivals.size() + " festivals");
        if (!festivals.isEmpty()) {
            fetchCompanies();
        }
    }

    private void fetchCompanies() {
        String selectedFestival = (String) festivalComboBox.getSelectedItem();
        String collectionName = "companies"; // or whatever your collection name is
        System.out.println("Fetching companies for festival: " + selectedFestival);
        companyController.getCompaniesByFestival(collectionName, selectedFestival, this::updateTable, this::handleError);
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        System.out.println("Performing search for: " + query);
        companyController.searchCompanies(query, this::updateTable, this::handleError);
    }

    private void updateTable(List<Company> companies) {
        System.out.println("Updating table with " + companies.size() + " companies");
        companyTableModel.setRowCount(0);
        for (Company company : companies) {
            companyTableModel.addRow(new Object[]{company.getId(), company.getCompanyName(), company.getProgramName()});
        }
    }

    private void openCompanyDetails(String companyId) {
        System.out.println("Opening details for company: " + companyId);
        companyController.getCompanyById(companyId,
                company -> mainFrame.showCompanyDetails(company),
                this::handleError
        );
    }

    private void handleError(Exception e) {
        System.err.println("Error: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void refresh() {
        loadFestivals();
    }
}