package org.example.models;

import com.google.cloud.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    private String id;
    private String text;
    private Timestamp timestamp;
    private String authorId; // Optional: if you want to track who made the comment

    public Comment() {
        // Default constructor for Firestore
    }

    public Comment(String text, String authorId) {
        this.text = text;
        this.authorId = authorId;
        this.timestamp = Timestamp.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    // toMap and fromMap methods for Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("text", text);
        map.put("timestamp", timestamp);
        map.put("authorId", authorId);
        return map;
    }

    public static Comment fromMap(Map<String, Object> map) {
        Comment comment = new Comment();
        comment.setId((String) map.get("id"));
        comment.setText((String) map.get("text"));
        comment.setTimestamp((Timestamp) map.get("timestamp"));
        comment.setAuthorId((String) map.get("authorId"));
        return comment;
    }
}