package com.daogenerator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ADDRESS_BOOK.
 */
public class AddressBook {

    private Long id;
    private String PeopleNo;
    private String PeopleName;
    private String LevelNum;
    private String ParentNo;
    private String FileName;
    private String PeopleTel;
    private String PeoplePhone;
    private String PeopleEmail;
    private String PeopleNote;
    private String TagId;
    private String TagName;
    private java.util.Date CreateDate;

    public AddressBook() {
    }

    public AddressBook(Long id) {
        this.id = id;
    }

    public AddressBook(Long id, String PeopleNo, String PeopleName, String LevelNum, String ParentNo, String FileName, String PeopleTel, String PeoplePhone, String PeopleEmail, String PeopleNote, String TagId, String TagName, java.util.Date CreateDate) {
        this.id = id;
        this.PeopleNo = PeopleNo;
        this.PeopleName = PeopleName;
        this.LevelNum = LevelNum;
        this.ParentNo = ParentNo;
        this.FileName = FileName;
        this.PeopleTel = PeopleTel;
        this.PeoplePhone = PeoplePhone;
        this.PeopleEmail = PeopleEmail;
        this.PeopleNote = PeopleNote;
        this.TagId = TagId;
        this.TagName = TagName;
        this.CreateDate = CreateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeopleNo() {
        return PeopleNo;
    }

    public void setPeopleNo(String PeopleNo) {
        this.PeopleNo = PeopleNo;
    }

    public String getPeopleName() {
        return PeopleName;
    }

    public void setPeopleName(String PeopleName) {
        this.PeopleName = PeopleName;
    }

    public String getLevelNum() {
        return LevelNum;
    }

    public void setLevelNum(String LevelNum) {
        this.LevelNum = LevelNum;
    }

    public String getParentNo() {
        return ParentNo;
    }

    public void setParentNo(String ParentNo) {
        this.ParentNo = ParentNo;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getPeopleTel() {
        return PeopleTel;
    }

    public void setPeopleTel(String PeopleTel) {
        this.PeopleTel = PeopleTel;
    }

    public String getPeoplePhone() {
        return PeoplePhone;
    }

    public void setPeoplePhone(String PeoplePhone) {
        this.PeoplePhone = PeoplePhone;
    }

    public String getPeopleEmail() {
        return PeopleEmail;
    }

    public void setPeopleEmail(String PeopleEmail) {
        this.PeopleEmail = PeopleEmail;
    }

    public String getPeopleNote() {
        return PeopleNote;
    }

    public void setPeopleNote(String PeopleNote) {
        this.PeopleNote = PeopleNote;
    }

    public String getTagId() {
        return TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public java.util.Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(java.util.Date CreateDate) {
        this.CreateDate = CreateDate;
    }

}