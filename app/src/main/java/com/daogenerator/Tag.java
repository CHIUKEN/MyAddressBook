package com.daogenerator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TAG.
 */
public class Tag {

    private Long id;
    private String TagId;
    private String TagName;

    public Tag() {
    }

    public Tag(Long id) {
        this.id = id;
    }

    public Tag(Long id, String TagId, String TagName) {
        this.id = id;
        this.TagId = TagId;
        this.TagName = TagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
