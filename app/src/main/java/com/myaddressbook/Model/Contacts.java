package com.myaddressbook.Model;

/**
 * Created by K on 2014/12/4.
 */
public class Contacts {
    private String contactsName;
    private String contactsPhone;
    private String contactsEmail;

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contacts) {
            if (this.getContactsName().equals(((Contacts) obj).getContactsName())) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
