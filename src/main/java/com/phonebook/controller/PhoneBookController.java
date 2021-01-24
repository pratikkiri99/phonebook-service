package com.phonebook.controller;

import com.phonebook.dto.Response;
import com.phonebook.model.PhoneBook;
import com.phonebook.service.PhoneBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController to expose phone book related services.
 */
@Slf4j
@Api(value = "Account Service API")
@RestController
@RequestMapping("/api")
public class PhoneBookController {

    PhoneBookService phoneBookService;

    public PhoneBookController(PhoneBookService phoneBookService) {
        this.phoneBookService = phoneBookService;
    }

    /**
     * Return all contacts from phoneBook.
     * @return
     */
    @GetMapping("/contacts")
    public List<PhoneBook> getAllContacts() {
        return phoneBookService.getAllContacts();
    }

    /**
     * Save contact.
     * @param phoneBook
     * @return
     */
    @PostMapping("/contacts/add")
    public ResponseEntity<String> addContact(@RequestBody PhoneBook phoneBook) {
        log.info(phoneBook.toString());
        return phoneBookService.saveContact(phoneBook);
    }

    /**
     * new Contacts are compared with existing Contacts.
     * @param newContacts
     * @return
     */
    @PostMapping("/contacts/unique")
    public Response getUniqueContacts(@RequestParam("newContacts") List<String> newContacts) {
        log.info("received contacts {} ",newContacts);
        return phoneBookService.getUniqueContacts(newContacts);
    }
}
