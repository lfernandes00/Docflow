package com.example.docflow;

public class RequestDataModel {
    int documentId, type;
    String name;

    public RequestDataModel(int documentId, int type, String name) {
        this.documentId = documentId;
        this.type = type;
        this.name = name;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
