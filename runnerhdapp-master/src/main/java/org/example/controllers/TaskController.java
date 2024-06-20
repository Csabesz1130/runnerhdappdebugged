package org.example.controllers;

import org.example.models.Company;
import org.example.services.FirestoreService;

import java.util.List;
import java.util.function.Consumer;

public class TaskController {
    private final FirestoreService firestoreService;

    public TaskController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public void createTask(Company company) {
        firestoreService.addTask("tasks", company);
    }

    public List<Company> getAllTasks() {
        return firestoreService.getTasks("tasks");
    }

    public void updateTask(Company company, String newStatus, String notes) {
        // Update the company object
        // Modify the field names according to your Company model
        // company.setStatus(newStatus);
        // company.setNotes(notes);

        // Call FirestoreService to update the company in Firebase
        firestoreService.updateTask("tasks", company.getId(), company);
    }

    public List<Company> getCompaniesByFestival(String collectionName, String selectedFestival) {
        return firestoreService.getCompaniesByFestival(collectionName, selectedFestival);
    }

    public List<String> getFestivals() {
        return firestoreService.getFestivals();
    }

    public Company getCompanyById(String collectionName, String companyId) {
        return firestoreService.getCompanyById(collectionName, companyId);
    }

    public void createCompany(String collectionName, Company company) {
        firestoreService.createCompany(collectionName, company);
    }

    public List<Company.Equipment> getEquipmentList(String collectionName, String companyId) {
        return firestoreService.getEquipmentList(collectionName, companyId);
    }

    public void updateEquipmentList(String collectionName, String companyId, List<Company.Equipment> equipmentList) {
        firestoreService.updateEquipmentList(collectionName, companyId, equipmentList);
    }

    // In TaskController.java
    public void getCompanyById(String collectionName, String companyId, Consumer<Company> onSuccess, Consumer<Exception> onFailure) {
        // Implementation
    }

    public void addCompanyListener(String collectionName, String companyId, Consumer<Company> onUpdate, Consumer<Exception> onError) {
        // Implementation
    }
}