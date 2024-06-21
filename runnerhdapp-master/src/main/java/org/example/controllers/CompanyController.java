package org.example.controllers;

import org.example.models.Company;
import org.example.models.Comment;
import org.example.services.FirestoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    public void getCompaniesByFestival(String collectionName, String festivalName, Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        firestoreService.getCompaniesByFestival(collectionName, festivalName, onSuccess, onFailure);
    }

    public List<String> getFestivals() {
        return firestoreService.getFestivals();
    }

    public void updateTask(Company company, String newStatus, String comment) {
        company.addDynamicField("status", newStatus);
        Comment newComment = new Comment(comment, "System");
        company.addComment(newComment);
        updateCompany(company, () -> {}, e -> logger.error("Error updating task", e));
    }

    public void searchCompanies(String query, Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        firestoreService.getAllCompanies(
                companies -> {
                    List<Company> searchResults = companies.stream()
                            .filter(company -> matchesSearch(company, query))
                            .collect(Collectors.toList());
                    logger.debug("Search query: '{}', Results: {}", query, searchResults.size());
                    onSuccess.accept(searchResults);
                },
                onFailure
        );
    }

    private boolean matchesSearch(Company company, String query) {
        query = query.toLowerCase();
        return (company.getCompanyName() != null && company.getCompanyName().toLowerCase().contains(query)) ||
                (company.getProgramName() != null && company.getProgramName().toLowerCase().contains(query)) ||
                (company.getId() != null && company.getId().toLowerCase().contains(query));
    }
}