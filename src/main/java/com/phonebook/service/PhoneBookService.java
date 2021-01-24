package com.phonebook.service;

import com.phonebook.dto.Response;
import com.phonebook.model.PhoneBook;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PhoneBookService {

    /**
     * Return all contacts from phoneBook
     * @return
     */
    List<PhoneBook> getAllContacts();

    /**
     * Save Contact
     * @return
     */
    ResponseEntity<String> saveContact(PhoneBook phoneBook);

    Response getUniqueContacts(List<String> newContacts);
}
