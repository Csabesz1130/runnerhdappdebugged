package org.example.controllers;

import org.example.models.Company;
import org.example.models.Comment;
import org.example.services.FirestoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

    public List<Company> getAllTasks() {
        CompletableFuture<List<Company>> future = new CompletableFuture<>();

        firestoreService.getAllCompanies(
                companies -> {
                    logger.info("Successfully retrieved {} companies", companies.size());
                    future.complete(companies);
                },
                exception -> {
                    logger.error("Error retrieving companies", exception);
                    future.completeExceptionally(exception);
                }
        );

        try {
            return future.get(); // This will block until the future is completed
        } catch (Exception e) {
            logger.error("Error while waiting for companies", e);
            throw new RuntimeException("Failed to retrieve companies", e);
        }
    }

    public void updateTask(Company company, String ujStatusz, String megjegyzes) {
    }
}