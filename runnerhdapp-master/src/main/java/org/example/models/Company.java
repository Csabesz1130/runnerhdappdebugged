package org.example.models;

import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Company {
    private String id;
    private String programName;
    private Timestamp lastModified;
    private String companyName;
    private List<Equipment> equipmentList;
    private Map<String, Object> dynamicFields;
    private String felderites;
    private String telepites;
    private boolean eloszto;
    private boolean aram;
    private boolean halozat;
    private boolean PTG;
    private boolean szoftver;
    private boolean param;
    private boolean helyszin;
    private String bontas;
    private boolean bazisLeszereles;
    private List<Comment> comments;


    public Company() {
        this.felderites = "TELEPÍTHETŐ";
        this.telepites = "KIADVA";
        this.bontas = "BONTHATÓ";
        this.dynamicFields = new HashMap<>();
        this.comments = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    @PropertyName("ProgramName")
    public String getProgramName() {
        return programName;
    }

    @PropertyName("ProgramName")
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @PropertyName("LastModified")
    public Timestamp getLastModified() {
        return lastModified;
    }

    @PropertyName("LastModified")
    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    @PropertyName("CompanyName")
    public String getCompanyName() {
        return companyName;
    }

    @PropertyName("CompanyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public Map<String, Object> getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(Map<String, Object> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    public void addDynamicField(String key, Object value) {
        this.dynamicFields.put(key, value);
    }

    public Object getDynamicField(String key) {
        return this.dynamicFields.get(key);
    }

    // New getters and setters
    public String getFelderites() {
        return felderites;
    }

    public void setFelderites(String felderites) {
        this.felderites = felderites;
    }

    public String getTelepites() {
        return telepites;
    }

    public void setTelepites(String telepites) {
        this.telepites = telepites;
    }

    public boolean isEloszto() {
        return eloszto;
    }

    public void setEloszto(boolean eloszto) {
        this.eloszto = eloszto;
    }

    public boolean isAram() {
        return aram;
    }

    public void setAram(boolean aram) {
        this.aram = aram;
    }

    public boolean isHalozat() {
        return halozat;
    }

    public void setHalozat(boolean halozat) {
        this.halozat = halozat;
    }

    public boolean isPTG() {
        return PTG;
    }

    public void setPTG(boolean PTG) {
        this.PTG = PTG;
    }

    public boolean isSzoftver() {
        return szoftver;
    }

    public void setSzoftver(boolean szoftver) {
        this.szoftver = szoftver;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }

    public boolean isHelyszin() {
        return helyszin;
    }

    public void setHelyszin(boolean helyszin) {
        this.helyszin = helyszin;
    }

    public String getBontas() {
        return bontas;
    }

    public void setBontas(String bontas) {
        this.bontas = bontas;
    }

    public boolean isBazisLeszereles() {
        return bazisLeszereles;
    }

    public void setBazisLeszereles(boolean bazisLeszereles) {
        this.bazisLeszereles = bazisLeszereles;
    }

    // toMap and fromMap methods
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("ProgramName", programName);
        map.put("LastModified", lastModified);
        map.put("CompanyName", companyName);
        map.put("equipmentList", equipmentList);
        map.put("dynamicFields", dynamicFields);
        map.put("felderites", felderites);
        map.put("telepites", telepites);
        map.put("eloszto", eloszto);
        map.put("aram", aram);
        map.put("halozat", halozat);
        map.put("PTG", PTG);
        map.put("szoftver", szoftver);
        map.put("param", param);
        map.put("helyszin", helyszin);
        map.put("bontas", bontas);
        map.put("bazisLeszereles", bazisLeszereles);
        map.put("comments", comments.stream().map(Comment::toMap).collect(Collectors.toList()));
        return map;
    }

    public static Company fromMap(Map<String, Object> map) {
        Company company = new Company();
        company.setId((String) map.get("id"));
        company.setProgramName((String) map.get("ProgramName"));
        company.setLastModified((Timestamp) map.get("LastModified"));
        company.setCompanyName((String) map.get("CompanyName"));
        company.setEquipmentList((List<Equipment>) map.get("equipmentList"));
        company.setDynamicFields((Map<String, Object>) map.get("dynamicFields"));
        company.setFelderites((String) map.getOrDefault("felderites", "TELEPÍTHETŐ"));
        company.setTelepites((String) map.getOrDefault("telepites", "KIADVA"));
        company.setEloszto((Boolean) map.getOrDefault("eloszto", false));
        company.setAram((Boolean) map.getOrDefault("aram", false));
        company.setHalozat((Boolean) map.getOrDefault("halozat", false));
        company.setPTG((Boolean) map.getOrDefault("PTG", false));
        company.setSzoftver((Boolean) map.getOrDefault("szoftver", false));
        company.setParam((Boolean) map.getOrDefault("param", false));
        company.setHelyszin((Boolean) map.getOrDefault("helyszin", false));
        company.setBontas((String) map.getOrDefault("bontas", "BONTHATÓ"));
        company.setBazisLeszereles((Boolean) map.getOrDefault("bazisLeszereles", false));
        List<Map<String, Object>> commentMaps = (List<Map<String, Object>>) map.get("comments");
        if (commentMaps != null) {
            company.setComments(commentMaps.stream().map(Comment::fromMap).collect(Collectors.toList()));
        }
        return company;
    }

    // Inner class for Equipment
    public static class Equipment {
        private Map<String, Object> dynamicFields;
        private String snDid;
        private String type;
        private String model;
        private String status;
        private char[] name;
        private char[] quantity;

        public Equipment() {
            this.dynamicFields = new HashMap<>();
        }

        public Equipment(String snDid, String type, String model, String status) {
            this();
            this.snDid = snDid;
            this.type = type;
            this.model = model;
            this.status = status;
        }

        // Getters and setters for all fields
        public String getSnDid() {
            return snDid;
        }

        public void setSnDid(String snDid) {
            this.snDid = snDid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public char[] getName() {
            return name;
        }

        public void setName(char[] name) {
            this.name = name;
        }

        public char[] getQuantity() {
            return quantity;
        }

        public void setQuantity(char[] quantity) {
            this.quantity = quantity;
        }

        public Map<String, Object> getDynamicFields() {
            return dynamicFields;
        }

        public void setDynamicFields(Map<String, Object> dynamicFields) {
            this.dynamicFields = dynamicFields;
        }

        public void addDynamicField(String key, Object value) {
            this.dynamicFields.put(key, value);
        }

        public Object getDynamicField(String key) {
            return this.dynamicFields.get(key);
        }

        // toMap and fromMap methods for Equipment
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("snDid", snDid);
            map.put("type", type);
            map.put("model", model);
            map.put("status", status);
            map.put("name", new String(name));
            map.put("quantity", new String(quantity));
            map.put("dynamicFields", dynamicFields);
            return map;
        }

        public static Equipment fromMap(Map<String, Object> map) {
            Equipment equipment = new Equipment();
            equipment.setSnDid((String) map.get("snDid"));
            equipment.setType((String) map.get("type"));
            equipment.setModel((String) map.get("model"));
            equipment.setStatus((String) map.get("status"));
            equipment.setName(((String) map.get("name")).toCharArray());
            equipment.setQuantity(((String) map.get("quantity")).toCharArray());
            equipment.setDynamicFields((Map<String, Object>) map.get("dynamicFields"));
            return equipment;
        }
    }
}