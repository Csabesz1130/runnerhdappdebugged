package org.example.views;

import org.example.controllers.TaskController;
import org.example.models.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class CompanyDetailsView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(CompanyDetailsView.class);
    private final TaskController taskController;
    private final String collectionName;
    private final String companyId;

    private JLabel nameLabel;
    private JLabel lastModifiedLabel;
    private JLabel programLabel;
    private JTextArea equipmentArea;

    public CompanyDetailsView(TaskController taskController, String collectionName, String companyId) {
        this.taskController = taskController;
        this.collectionName = collectionName;
        this.companyId = companyId;

        initComponents();
        fetchCompanyDetails();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        add(new JLabel("Name:"), gbc);
        gbc.gridy++;
        add(new JLabel("Last Modified:"), gbc);
        gbc.gridy++;
        add(new JLabel("Program:"), gbc);
        gbc.gridy++;
        add(new JLabel("Equipment:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        nameLabel = new JLabel();
        add(nameLabel, gbc);
        gbc.gridy++;
        lastModifiedLabel = new JLabel();
        add(lastModifiedLabel, gbc);
        gbc.gridy++;
        programLabel = new JLabel();
        add(programLabel, gbc);
        gbc.gridy++;

        equipmentArea = new JTextArea(5, 20);
        equipmentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(equipmentArea);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void fetchCompanyDetails() {
        logger.info("Fetching company details for ID: {} from collection: {}", companyId, collectionName);

        taskController.getCompanyById(collectionName, companyId,
                this::displayCompanyDetails,
                error -> handleError("Error fetching company details", error)
        );
    }

    private void displayCompanyDetails(Task company) {
        SwingUtilities.invokeLater(() -> {
            nameLabel.setText(company.getCompanyName());
            lastModifiedLabel.setText(company.getLastModified() != null ? company.getLastModified().toString() : "N/A");
            programLabel.setText(company.getProgramName());

            StringBuilder equipmentText = new StringBuilder();
            for (Task.Equipment equipment : company.getEquipmentList()) {
                equipmentText.append(equipment.getName())
                        .append(" - Quantity: ")
                        .append(equipment.getQuantity())
                        .append("\n");
            }
            equipmentArea.setText(equipmentText.toString());
        });

        setupRealTimeListener();
    }

    private void setupRealTimeListener() {
        taskController.addCompanyListener(collectionName, companyId,
                updatedCompany -> {
                    logger.info("Received real-time update for company");
                    displayCompanyDetails(updatedCompany);
                },
                error -> handleError("Error in real-time listener", error)
        );
    }

    private void handleError(String context, Throwable error) {
        String errorMessage = (error instanceof Exception) ? ((Exception) error).getMessage() : "Unknown error";
        logger.error("{}: {}", context, errorMessage);
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, context + ": " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE));
    }
}