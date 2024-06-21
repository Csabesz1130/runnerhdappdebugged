package org.example.views;

import org.example.MainFrame;
import org.example.controllers.AuthController;
import org.example.controllers.CompanyController;
import org.example.models.Company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JTextField searchField;
    private JButton searchButton;

    public MainView(AuthController authController, MainFrame mainFrame, CompanyController companyController) {
        this.authController = authController;
        this.mainFrame = mainFrame;
        this.companyController = companyController;
        initComponents();
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
        festivalComboBox.addActionListener(e -> fetchCompanies());
        topPanel.add(festivalComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        installationRadioButton = new JRadioButton("Company_Install");
        installationRadioButton.addActionListener(e -> fetchCompanies());
        topPanel.add(installationRadioButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        demolitionRadioButton = new JRadioButton("Company_Demolition");
        demolitionRadioButton.addActionListener(e -> fetchCompanies());
        topPanel.add(demolitionRadioButton, gbc);

        ButtonGroup choiceButtonGroup = new ButtonGroup();
        choiceButtonGroup.add(installationRadioButton);
        choiceButtonGroup.add(demolitionRadioButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, gbc);

        add(topPanel, BorderLayout.NORTH);

        companyTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Last Modified", "Program"}, 0);
        companyTable = new JTable(companyTableModel);
        JScrollPane companyScrollPane = new JScrollPane(companyTable);
        add(companyScrollPane, BorderLayout.CENTER);

        addTableContextMenu();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        bottomPanel.add(logoutButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addTableContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem viewDetailsItem = new JMenuItem("View Details");
        viewDetailsItem.addActionListener(e -> {
            int selectedRow = companyTable.getSelectedRow();
            if (selectedRow != -1) {
                String companyId = (String) companyTableModel.getValueAt(selectedRow, 0);
                openCompanyDetailsView(companyId);
            }
        });
        contextMenu.add(viewDetailsItem);

        companyTable.setComponentPopupMenu(contextMenu);

        companyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = companyTable.rowAtPoint(e.getPoint());
                    if (selectedRow != -1) {
                        String companyId = (String) companyTableModel.getValueAt(selectedRow, 0);
                        openCompanyDetailsView(companyId);
                    }
                }
            }
        });
    }

    private void openCompanyDetailsView(String companyId) {
        companyController.getCompanyById(companyId,
                company -> mainFrame.showCompanyDetails(company),
                error -> JOptionPane.showMessageDialog(this,
                        "Error loading company details: " + error.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE)
        );
    }

    public void loadFestivals() {
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
                this::setTasks,
                error -> JOptionPane.showMessageDialog(this,
                        "Error fetching companies: " + error.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE)
        );
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            fetchCompanies();
            return;
        }

        companyController.searchCompanies(
                query,
                this::setTasks,
                error -> SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Error performing search: " + error.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE)
                )
        );
    }

    private void logout() {
        authController.logout();
        mainFrame.showLoginView();
    }

    public void refresh() {
        fetchCompanies();
    }
}