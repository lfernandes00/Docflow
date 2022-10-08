package com.example.docflow;

import java.util.Date;

public class DocumentDataModel {
    int id;
    int userId;
    int typeId;
    int version;
    int pending;
    int value;
    int clientId;
    int folderId;
    int deleted;
    String dataVencimento;
    String name, description, extension;

    public DocumentDataModel(int id, String name, int userId, String dataVencimento, int typeId, int version, int pending, int value, String description, int clientId, String extension, int folderId, int deleted) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.dataVencimento = dataVencimento;
        this.typeId = typeId;
        this.version = version;
        this.pending = pending;
        this.value = value;
        this.description = description;
        this.clientId = clientId;
        this.extension = extension;
        this.folderId = folderId;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
}
