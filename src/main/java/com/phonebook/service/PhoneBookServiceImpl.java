package com.phonebook.service;

import com.phonebook.dto.Response;
import com.phonebook.dao.PhoneBookRepository;
import com.phonebook.exception.PhoneBookServiceException;
import com.phonebook.model.PhoneBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.phonebook.exception.Constants.*;

@Slf4j
@Service
public class PhoneBookServiceImpl implements PhoneBookService {

    PhoneBookRepository phoneBookRepository;

    public PhoneBookServiceImpl(PhoneBookRepository phoneBookRepository) {
        this.phoneBookRepository = phoneBookRepository;
    }

    /**
     * Return all contacts from phoneBook
     * @return
     */
    @Override
    public List<PhoneBook> getAllContacts() {

        List<PhoneBook> contacts = phoneBookRepository.findAll();
        if (contacts !=null && contacts.size() != 0) {
            log.info("Contact list found of size ", contacts.size());
            return contacts;
        } else {
            log.warn(NO_CONTACTS);
            throw new PhoneBookServiceException(NO_CONTACTS);
        }
    }

    /**
     * Save Contact
     * @return
     */
    @Override
    public ResponseEntity<String> saveContact(PhoneBook phoneBook) {
        if(validatePhoneNumber(phoneBook.getPhoneNumber()) && validateName(phoneBook.getName())) {
            PhoneBook phoneBookResp = phoneBookRepository.save(phoneBook);
            String msg = phoneBookResp.getName() + " saved successfully";
            return new ResponseEntity<String>(msg,HttpStatus.CREATED);
        }
        return null;
    }

    @Override
    public Response getUniqueContacts(List<String> newContacts) {
        List<PhoneBook> allContacts = getAllContacts();
        List<String> existingContacts = allContacts.stream().map(PhoneBook::getName).collect(Collectors.toList());
        Set<String> existingContactsSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        existingContactsSet.addAll(existingContacts);
        Set<String> newContactsSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        newContactsSet.addAll(newContacts);

        // treeset to compare with ignore case
        existingContactsSet.removeAll(newContactsSet);
        Response response = new Response();
        response.getUniqueContacts().put("Existing", existingContactsSet);

        Set<String> existingContactsSet1 = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        existingContactsSet1.addAll(existingContacts);
        newContactsSet.removeAll(existingContactsSet1);
        response.getUniqueContacts().put("New", newContactsSet);

        return response;
    }


    private boolean validatePhoneNumber(String phoneNumber) {
        // validate phone numbers of format "1234567890"
        if (phoneNumber.matches("\\d{9}")) {
            log.info("valid phone format");
            List<PhoneBook> contacts = phoneBookRepository.findByPhoneNumber(phoneNumber);
            if(contacts != null && contacts.size() > 0) {
                log.warn(PHONE_ALREADY_EXISTS);
                throw new PhoneBookServiceException(PHONE_ALREADY_EXISTS);
            } else {
                log.info("Valid phone number");
                return true;
            }
        } else {
            log.warn(INVALID_PHONE_FORMAT);
            throw new PhoneBookServiceException(INVALID_PHONE_FORMAT);
        }
    }

    private boolean validateName(String name) {
        if(name !=null && !StringUtils.isEmpty(name) && !name.matches(".*\\d.*") && !name.contains(" ")) {
            List<PhoneBook> contacts = phoneBookRepository.findByNameInIgnoreCase(name);
            if(contacts != null && contacts.size() > 0) {
                log.warn(NAME_ALREADY_EXISTS);
                throw new PhoneBookServiceException(NAME_ALREADY_EXISTS);
            } else {
                log.info("Valid name");
                return true;
            }
        }  else {
            log.warn(INVALID_NAME);
            throw new PhoneBookServiceException(INVALID_NAME);
        }
    }

}
