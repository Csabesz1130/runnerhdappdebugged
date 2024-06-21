package org.example.controllers;

import org.example.models.Company;
import org.example.models.Comment;
import org.example.services.FirestoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final FirestoreService firestoreService;

    public CompanyController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public void getCompanyById(String companyId, Consumer<Company> onSuccess, Consumer<Exception> onFailure) {
        firestoreService.getCompanyById(companyId, onSuccess, onFailure);
    }

    public void updateCompany(Company company, Runnable onSuccess, Consumer<Exception> onFailure) {
        firestoreService.updateCompany(company, onSuccess, onFailure);
    }

    public void deleteCompany(String companyId, Runnable onSuccess, Consumer<Exception> onFailure) {
        firestoreService.deleteCompany(companyId, onSuccess, onFailure);
    }

    public void addComment(String companyId, Comment newComment, Runnable onSuccess, Consumer<Exception> onFailure) {
        firestoreService.addComment(companyId, newComment, onSuccess, onFailure);
    }

    public void addCompanyListener(String companyId, Consumer<Company> onUpdate, Consumer<Exception> onError) {
        firestoreService.addCompanyListener(companyId, onUpdate, onError);
    }

    public void getCompaniesByFestival(String collectionName, String festivalName, Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        firestoreService.getCompaniesByFestival(collectionName, festivalName, onSuccess, onFailure);
    }

    public List<String> getFestivals() {
        return firestoreService.getFestivals();
    }

    public void createCompany(String collectionName, Company company, Runnable onSuccess, Consumer<Exception> onFailure) {
        firestoreService.createCompany(collectionName, company, onSuccess, onFailure);
    }

    public void updateEquipmentList(String collectionName, String companyId, List<Company.Equipment> equipmentList, Runnable onSuccess, Consumer<Exception> onFailure) {
        firestoreService.updateEquipmentList(collectionName, companyId, equipmentList, onSuccess, onFailure);
    }

    public void getAllCompanies(Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        firestoreService.getAllCompanies(onSuccess, onFailure);
    }

    public void updateCompanyStatus(String companyId, String newStatus, String comment, Runnable onSuccess, Consumer<Exception> onFailure) {
        getCompanyById(companyId,
                company -> {
                    company.addDynamicField("status", newStatus);
                    Comment statusComment = new Comment(comment, "System");
                    company.addComment(statusComment);
                    updateCompany(company, onSuccess, onFailure);
                },
                onFailure
        );
    }

    public void searchCompanies(String query, Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        final String searchQuery = query.toLowerCase(); // Create an effectively final copy
        getAllCompanies(
                companies -> {
                    List<Company> searchResults = companies.stream()
                            .filter(company -> matchesSearch(company, searchQuery))
                            .collect(Collectors.toList());
                    onSuccess.accept(searchResults);
                },
                onFailure
        );
    }

    private boolean matchesSearch(Company company, String query) {
        return (company.getId() != null && company.getId().toLowerCase().contains(query)) ||
                (company.getCompanyName() != null && company.getCompanyName().toLowerCase().contains(query)) ||
                (company.getProgramName() != null && company.getProgramName().toLowerCase().contains(query)) ||
                (company.getEquipmentList() != null && company.getEquipmentList().stream()
                        .anyMatch(equipment -> equipment.getSnDid() != null && equipment.getSnDid().toLowerCase().contains(query)));
    }

    public void updateTask(Company company, String newStatus, String comment) {
        company.addDynamicField("status", newStatus);
        Comment newComment = new Comment(comment, "System");
        company.addComment(newComment);
        updateCompany(company, () -> {}, e -> logger.error("Error updating task", e));
    }

    public List<Company> getAllTasks() {
        AtomicReference<List<Company>> result = new AtomicReference<>();
        getAllCompanies(
                result::set,
                e -> logger.error("Error getting all tasks", e)
        );
        return result.get();
    }
}