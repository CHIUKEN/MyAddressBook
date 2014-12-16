package com.myaddressbook.Model;

import java.util.ArrayList;

/**
 * Created by K on 2014/12/4.
 */
public class Contacts {
    public String id;

    private String contactsName;
    private String contactsPhone;
    private String contactsEmail;
    public ArrayList<ContactEmail> emails;
    public ArrayList<ContactPhone> numbers;

    public Contacts() {

    }

    public Contacts(String id, String name) {
        this.id = id;
        this.contactsName = name;
        this.emails = new ArrayList<ContactEmail>();
        this.numbers = new ArrayList<ContactPhone>();
    }

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
            } else {
                return false;
            }
        }
        return false;
    }

    public void addEmail(String address, String type) {
        emails.add(new ContactEmail(address, type));
    }

    public void addNumber(String number, String type) {
        numbers.add(new ContactPhone(number, type));
    }

    @Override
    public String toString() {
        String result = contactsName;
        if (numbers.size() > 0) {
            ContactPhone number = numbers.get(0);
            result += " (" + number.number + " - " + number.type + ")";
        }
        if (emails.size() > 0) {
            ContactEmail email = emails.get(0);
            result += " [" + email.address + " - " + email.type + "]";
        }
        return result;
    }

}
