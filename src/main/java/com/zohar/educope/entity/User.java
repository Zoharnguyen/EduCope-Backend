package com.zohar.educope.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zohar.educope.constant.UserType;
import com.zohar.educope.dto.AdjustUserProfile;
import com.zohar.educope.dto.ChatOverview;
import com.zohar.educope.dto.NotificationElement;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "User")
public class User {

    @Id
    public String id;

    @Field(name = "gmail")
    public String gmail;

    @Field(name = "password")
    public String password;

    @Field(name = "usertype")
    public UserType userType;

    @Field(name = "fullName")
    private String fullName;

    @Field(name = "rate")
    private String rate;

    @Field(name = "introduction")
    private String introduction;

    @Field(name = "urlImageProfile")
    private String urlImageProfile;

    @Field(name = "phone")
    private String phone;

    @Field(name = "certificate")
    private String certificate;

    @Field(name = "DoB")
    private String DoB;

    @Field(name = "experience")
    private String experience;

    @Field(name = "gender")
    private String gender;

    @Field(name = "address")
    private String address;

    @Field(name = "adjustUserProfileList")
    private List<AdjustUserProfile> adjustUserProfileList;

    @Field(name = "topicNotification")
    private String topicNotification;

    @Field(name = "notifications")
    private List<NotificationElement> notifications;

    @Field(name = "chatOverviews")
    private List<ChatOverview> chatOverviews;

    public List<ChatOverview> getChatOverviews() {
        return chatOverviews;
    }

    public void setChatOverviews(List<ChatOverview> chatOverviews) {
        this.chatOverviews = chatOverviews;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<NotificationElement> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationElement> notifications) {
        this.notifications = notifications;
    }

    public String getTopicNotification() {
        return topicNotification;
    }

    public void setTopicNotification(String topicNotification) {
        this.topicNotification = topicNotification;
    }

    public List<AdjustUserProfile> getAdjustUserProfileList() {
        return adjustUserProfileList;
    }

    public void setAdjustUserProfileList(
        List<AdjustUserProfile> adjustUserProfileList) {
        this.adjustUserProfileList = adjustUserProfileList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @CreatedDate
    @Field(name = "createdAt")
    @JsonIgnore
    private DateTime createdAt;

    @LastModifiedDate
    @Field(name = "updatedAt")
    @JsonIgnore
    private DateTime updatedAt;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName= fullName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUrlImageProfile() {
        return urlImageProfile;
    }

    public void setUrlImageProfile(String urlImageProfile) {
        this.urlImageProfile = urlImageProfile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
