package org.example.services;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.ApiFutureCallback;
import com.google.cloud.firestore.*;
import com.google.common.util.concurrent.MoreExecutors;
import org.example.models.Company;
import org.example.models.User;
import org.example.models.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class FirestoreService {
    private static final Logger logger = LoggerFactory.getLogger(FirestoreService.class);
    private final Firestore db;
    private User loggedInUser;

    public FirestoreService(Firestore db) {
        this.db = db;
    }

    public boolean isInitialized() {
        return db != null;
    }

    public boolean login(String username, String password) {
        if (db != null) {
            CollectionReference usersCollection = db.collection("Users");
            Query query = usersCollection.whereEqualTo("username", username).whereEqualTo("password", password);
            ApiFuture<QuerySnapshot> future = query.get();
            try {
                QuerySnapshot snapshot = future.get();
                if (!snapshot.isEmpty()) {
                    loggedInUser = snapshot.getDocuments().get(0).toObject(User.class);
                    return true;
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to login user: {}", e.getMessage());
            }
        } else {
            logger.error("Firestore is not initialized. Cannot login user.");
        }
        return false;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public String getLoggedInUser() {
        if (loggedInUser != null) {
            return loggedInUser.getUsername();
        }
        return null;
    }

    public void logout() {
        loggedInUser = null;
    }

    public void getCompanyById(String companyId, Consumer<Company> onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            DocumentReference companyDocument = db.collection("companies").document(companyId);
            ApiFuture<DocumentSnapshot> future = companyDocument.get();
            ApiFutures.addCallback(future, new ApiFutureCallback<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot document) {
                    if (document.exists()) {
                        Company company = document.toObject(Company.class);
                        onSuccess.accept(company);
                    } else {
                        onFailure.accept(new Exception("Company not found"));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public void updateCompany(Company company, Runnable onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<WriteResult> future = db.collection("companies").document(company.getId()).set(company);
            ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
                @Override
                public void onSuccess(WriteResult result) {
                    onSuccess.run();
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public void deleteCompany(String companyId, Runnable onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<WriteResult> future = db.collection("companies").document(companyId).delete();
            ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
                @Override
                public void onSuccess(WriteResult result) {
                    onSuccess.run();
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public void addComment(String companyId, Comment comment, Runnable onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<WriteResult> future = db.collection("companies").document(companyId)
                    .update("comments", FieldValue.arrayUnion(comment));
            ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
                @Override
                public void onSuccess(WriteResult result) {
                    onSuccess.run();
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public void addCompanyListener(String companyId, Consumer<Company> onUpdate, Consumer<Exception> onError) {
        if (db != null) {
            db.collection("companies").document(companyId)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            onError.accept(e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Company company = snapshot.toObject(Company.class);
                            onUpdate.accept(company);
                        } else {
                            onError.accept(new Exception("Company does not exist"));
                        }
                    });
        } else {
            onError.accept(new Exception("Firestore is not initialized"));
        }
    }

    public void getCompaniesByFestival(String collectionName, String festivalName, Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<QuerySnapshot> future = db.collection(collectionName)
                    .whereEqualTo("ProgramName", festivalName)
                    .get();
            ApiFutures.addCallback(future, new ApiFutureCallback<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    List<Company> companies = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        companies.add(document.toObject(Company.class));
                    }
                    onSuccess.accept(companies);
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public List<String> getFestivals() {
        List<String> festivals = new ArrayList<>();
        if (db != null) {
            CollectionReference programsCollection = db.collection("Programs");
            try {
                QuerySnapshot querySnapshot = programsCollection.get().get();
                for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                    String festivalName = document.getString("ProgramName");
                    festivals.add(festivalName);
                }
            } catch (Exception e) {
                logger.error("Error retrieving festivals: {}", e.getMessage());
            }
        } else {
            logger.error("Firestore is not initialized. Cannot retrieve festivals.");
        }
        return festivals;
    }

    public void createCompany(String collectionName, Company company, Runnable onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<WriteResult> future = db.collection(collectionName).document(company.getId()).set(company);
            ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
                @Override
                public void onSuccess(WriteResult result) {
                    onSuccess.run();
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public void updateEquipmentList(String collectionName, String companyId, List<Company.Equipment> equipmentList, Runnable onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<WriteResult> future = db.collection(collectionName).document(companyId)
                    .update("equipmentList", equipmentList);
            ApiFutures.addCallback(future, new ApiFutureCallback<WriteResult>() {
                @Override
                public void onSuccess(WriteResult result) {
                    onSuccess.run();
                }

                @Override
                public void onFailure(Throwable t) {
                    onFailure.accept(new Exception(t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }

    public List<Company.Equipment> getEquipmentList(String collectionName, String companyId) {
        List<Company.Equipment> equipmentList = new ArrayList<>();
        if (db != null) {
            DocumentReference companyDocument = db.collection(collectionName).document(companyId);
            try {
                DocumentSnapshot documentSnapshot = companyDocument.get().get();
                if (documentSnapshot.exists()) {
                    Company company = documentSnapshot.toObject(Company.class);
                    if (company != null) {
                        equipmentList = company.getEquipmentList();
                    }
                }
            } catch (Exception e) {
                logger.error("Error retrieving equipment list: {}", e.getMessage());
            }
        } else {
            logger.error("Firestore is not initialized. Cannot retrieve equipment list.");
        }
        return equipmentList;
    }

    public void getAllCompanies(Consumer<List<Company>> onSuccess, Consumer<Exception> onFailure) {
        if (db != null) {
            ApiFuture<QuerySnapshot> future = db.collection("companies").get();
            ApiFutures.addCallback(future, new ApiFutureCallback<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    List<Company> companies = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Company company = document.toObject(Company.class);
                        if (company != null) {
                            companies.add(company);
                        }
                    }
                    logger.info("Successfully retrieved {} companies", companies.size());
                    onSuccess.accept(companies);
                }

                @Override
                public void onFailure(Throwable t) {
                    logger.error("Error retrieving companies", t);
                    onFailure.accept(new Exception("Failed to retrieve companies", t));
                }
            }, MoreExecutors.directExecutor());
        } else {
            logger.error("Firestore is not initialized");
            onFailure.accept(new Exception("Firestore is not initialized"));
        }
    }
}