package org.example.views;

import org.example.controllers.CompanyController;
import org.example.models.Company;
import org.example.models.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CompanyDetailsView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(CompanyDetailsView.class);
    private final CompanyController companyController;
    private final String companyId;
    private Company company;

    // UI components
    private JTextField companyNameField;
    private JTextField programNameField;
    private JComboBox<String> felderitesComboBox;
    private JComboBox<String> telepitesComboBox;
    private JCheckBox elosztoCheckBox, aramCheckBox, halozatCheckBox, PTGCheckBox,
            szoftverCheckBox, paramCheckBox, helyszinCheckBox, bazisLeszerelesCheckBox;
    private JComboBox<String> bontasComboBox;
    private JLabel lastModifiedLabel;
    private JTextArea commentsArea;
    private JTextField newCommentField;
    private JButton addCommentButton, editButton, saveButton, cancelButton, deleteButton, refreshButton;

    public CompanyDetailsView(CompanyController companyController, String companyId) {
        this.companyController = companyController;
        this.companyId = companyId;
        initComponents();
        fetchCompanyDetails();
        setupRealTimeListener();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Main details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to detailsPanel
        addLabelAndComponent(detailsPanel, gbc, "Company ID:", new JLabel(companyId));
        companyNameField = addLabelAndTextField(detailsPanel, gbc, "Company Name:");
        programNameField = addLabelAndTextField(detailsPanel, gbc, "Program Name:");
        felderitesComboBox = addLabelAndComboBox(detailsPanel, gbc, "Felderites:",
                new String[]{"TELEPÍTHETŐ", "KIRAKHATÓ", "NEM KIRAKHATÓ"});
        telepitesComboBox = addLabelAndComboBox(detailsPanel, gbc, "Telepites:",
                new String[]{"KIADVA", "OPTION1", "OPTION2"}); // Add actual options
        elosztoCheckBox = addCheckBox(detailsPanel, gbc, "Eloszto");
        aramCheckBox = addCheckBox(detailsPanel, gbc, "Aram");
        halozatCheckBox = addCheckBox(detailsPanel, gbc, "Halozat");
        PTGCheckBox = addCheckBox(detailsPanel, gbc, "PTG");
        szoftverCheckBox = addCheckBox(detailsPanel, gbc, "Szoftver");
        paramCheckBox = addCheckBox(detailsPanel, gbc, "Param");
        helyszinCheckBox = addCheckBox(detailsPanel, gbc, "Helyszin");
        bontasComboBox = addLabelAndComboBox(detailsPanel, gbc, "Bontas:",
                new String[]{"BONTHATÓ", "OPTION1", "OPTION2"}); // Add actual options
        bazisLeszerelesCheckBox = addCheckBox(detailsPanel, gbc, "Bazis Leszereles");
        lastModifiedLabel = addLabelAndComponent(detailsPanel, gbc, "Last Modified:", new JLabel());

        // Comments section
        JPanel commentsPanel = new JPanel(new BorderLayout());
        commentsArea = new JTextArea(10, 30);
        commentsArea.setEditable(false);
        JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
        commentsPanel.add(commentsScrollPane, BorderLayout.CENTER);

        JPanel addCommentPanel = new JPanel(new BorderLayout());
        newCommentField = new JTextField(20);
        addCommentButton = new JButton("Add Comment");
        addCommentPanel.add(newCommentField, BorderLayout.CENTER);
        addCommentPanel.add(addCommentButton, BorderLayout.EAST);
        commentsPanel.add(addCommentPanel, BorderLayout.SOUTH);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editButton = new JButton("Edit");
        saveButton = new JButton("Save Changes");
        cancelButton = new JButton("Cancel");
        deleteButton = new JButton("Delete Company");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Add all panels to main panel
        add(detailsPanel, BorderLayout.NORTH);
        add(commentsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set up action listeners
        setupActionListeners();

        // Initially disable editing
        setEditingEnabled(false);
    }

    private void setupActionListeners() {
        editButton.addActionListener(e -> setEditingEnabled(true));
        saveButton.addActionListener(e -> saveChanges());
        cancelButton.addActionListener(e -> cancelChanges());
        deleteButton.addActionListener(e -> deleteCompany());
        refreshButton.addActionListener(e -> fetchCompanyDetails());
        addCommentButton.addActionListener(e -> addComment());
    }

    private void fetchCompanyDetails() {
        companyController.getCompanyById(companyId, this::displayCompanyDetails, this::handleError);
    }

    private void displayCompanyDetails(Company company) {
        this.company = company;
        SwingUtilities.invokeLater(() -> {
            companyNameField.setText(company.getCompanyName());
            programNameField.setText(company.getProgramName());
            felderitesComboBox.setSelectedItem(company.getFelderites());
            telepitesComboBox.setSelectedItem(company.getTelepites());
            elosztoCheckBox.setSelected(company.isEloszto());
            aramCheckBox.setSelected(company.isAram());
            halozatCheckBox.setSelected(company.isHalozat());
            PTGCheckBox.setSelected(company.isPTG());
            szoftverCheckBox.setSelected(company.isSzoftver());
            paramCheckBox.setSelected(company.isParam());
            helyszinCheckBox.setSelected(company.isHelyszin());
            bontasComboBox.setSelectedItem(company.getBontas());
            bazisLeszerelesCheckBox.setSelected(company.isBazisLeszereles());
            lastModifiedLabel.setText(company.getLastModified().toString());
            displayComments(company.getComments());
        });
    }

    private void displayComments(List<Comment> comments) {
        StringBuilder sb = new StringBuilder();
        for (Comment comment : comments) {
            sb.append(comment.getTimestamp())
                    .append(" - ")
                    .append(comment.getAuthorId())
                    .append(": ")
                    .append(comment.getText())
                    .append("\n");
        }
        commentsArea.setText(sb.toString());
    }

    private void setEditingEnabled(boolean enabled) {
        companyNameField.setEditable(enabled);
        programNameField.setEditable(enabled);
        felderitesComboBox.setEnabled(enabled);
        telepitesComboBox.setEnabled(enabled);
        elosztoCheckBox.setEnabled(enabled);
        aramCheckBox.setEnabled(enabled);
        halozatCheckBox.setEnabled(enabled);
        PTGCheckBox.setEnabled(enabled);
        szoftverCheckBox.setEnabled(enabled);
        paramCheckBox.setEnabled(enabled);
        helyszinCheckBox.setEnabled(enabled);
        bontasComboBox.setEnabled(enabled);
        bazisLeszerelesCheckBox.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }

    private void saveChanges() {
        updateCompanyFromUI();
        companyController.updateCompany(company, () -> {
            JOptionPane.showMessageDialog(this, "Changes saved successfully");
            setEditingEnabled(false);
        }, this::handleError);
    }

    private void cancelChanges() {
        displayCompanyDetails(company);
        setEditingEnabled(false);
    }

    private void deleteCompany() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this company?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            companyController.deleteCompany(companyId, () -> {
                JOptionPane.showMessageDialog(this, "Company deleted successfully");
                // TODO: Close this view and update the main view
            }, this::handleError);
        }
    }

    private void addComment() {
        String commentText = newCommentField.getText().trim();
        if (!commentText.isEmpty()) {
            Comment newComment = new Comment(commentText, "CurrentUserId"); // Replace with actual user ID
            companyController.addComment(company.getId(), newComment,
                    () -> {
                        company.addComment(newComment);
                        displayComments(company.getComments());
                        newCommentField.setText("");
                    },
                    this::handleError
            );
        }
    }

    private void updateCompanyFromUI() {
        company.setCompanyName(companyNameField.getText());
        company.setProgramName(programNameField.getText());
        company.setFelderites((String) felderitesComboBox.getSelectedItem());
        company.setTelepites((String) telepitesComboBox.getSelectedItem());
        company.setEloszto(elosztoCheckBox.isSelected());
        company.setAram(aramCheckBox.isSelected());
        company.setHalozat(halozatCheckBox.isSelected());
        company.setPTG(PTGCheckBox.isSelected());
        company.setSzoftver(szoftverCheckBox.isSelected());
        company.setParam(paramCheckBox.isSelected());
        company.setHelyszin(helyszinCheckBox.isSelected());
        company.setBontas((String) bontasComboBox.getSelectedItem());
        company.setBazisLeszereles(bazisLeszerelesCheckBox.isSelected());
    }

    private void setupRealTimeListener() {
        companyController.addCompanyListener(companyId, this::displayCompanyDetails, this::handleError);
    }

    private void handleError(Exception e) {
        logger.error("An error occurred", e);
        JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Helper methods for adding components
    private JTextField addLabelAndTextField(JPanel panel, GridBagConstraints gbc, String labelText) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField(20);
        panel.add(label, gbc);
        gbc.gridx++;
        panel.add(textField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        return textField;
    }

    private JComboBox<String> addLabelAndComboBox(JPanel panel, GridBagConstraints gbc, String labelText, String[] options) {
        JLabel label = new JLabel(labelText);
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(label, gbc);
        gbc.gridx++;
        panel.add(comboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        return comboBox;
    }

    private JCheckBox addCheckBox(JPanel panel, GridBagConstraints gbc, String labelText) {
        JCheckBox checkBox = new JCheckBox(labelText);
        gbc.gridwidth = 2;
        panel.add(checkBox, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        return checkBox;
    }

    private JLabel addLabelAndComponent(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);
        gbc.gridx++;
        panel.add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        return label;
    }
}