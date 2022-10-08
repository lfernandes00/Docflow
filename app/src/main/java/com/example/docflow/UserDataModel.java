package com.example.docflow;

public class UserDataModel {
    int id, typeId, reviewCount, approvalCount, workerNumber, uploadCount ;
    String email, password, name, photo, timeReview, timeApproval;

    public UserDataModel(int id, String email, String password, String name, int typeId, int reviewCount, int approvalCount, int workerNumber, int uploadCount, String photo, String timeReview, String timeApproval) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.typeId = typeId;
        this.reviewCount = reviewCount;
        this.approvalCount = approvalCount;
        this.workerNumber = workerNumber;
        this.uploadCount = uploadCount;
        this.photo = photo;
        this.timeReview = timeReview;
        this.timeApproval = timeApproval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getApprovalCount() {
        return approvalCount;
    }

    public void setApprovalCount(int approvalCount) {
        this.approvalCount = approvalCount;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getTimeReview() {
        return timeReview;
    }

    public void setTimeReview(String timeReview) {
        this.timeReview = timeReview;
    }

    public String getTimeApproval() {
        return timeApproval;
    }

    public void setTimeApproval(String timeApproval) {
        this.timeApproval = timeApproval;
    }
}
