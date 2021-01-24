package com.phonebook.service;

import com.phonebook.dao.PhoneBookRepository;
import com.phonebook.dto.Response;
import com.phonebook.exception.Constants;
import com.phonebook.exception.PhoneBookServiceException;
import com.phonebook.model.PhoneBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class PhoneBookServiceTest {

    PhoneBookRepository phoneBookRepository = Mockito.mock(PhoneBookRepository.class);

    PhoneBookService phoneBookService;

    @BeforeEach
    public void setUp() {
        phoneBookService = new PhoneBookServiceImpl(phoneBookRepository);
    }

    @Test
    public void getAllContactsTestWhenNoData() {
        Mockito.when(phoneBookRepository.findAll()).thenReturn(null);
        Exception e = Assertions.assertThrows(PhoneBookServiceException.class, ()->phoneBookService.getAllContacts());
        Assertions.assertEquals(e.getMessage(), Constants.NO_CONTACTS);
    }

    @Test
    public void getAllContactsTest() {
        PhoneBook phoneBook1 = new PhoneBook(1,"MockUser1", "123456789");
        PhoneBook phoneBook2 = new PhoneBook(2,"MockUser2", "123456789");
        List<PhoneBook> phoneBookList = new ArrayList<>();
        phoneBookList.add(phoneBook1);
        phoneBookList.add(phoneBook2);
        Mockito.when(phoneBookRepository.findAll()).thenReturn(phoneBookList);
        List<PhoneBook> phoneBookListResp = phoneBookService.getAllContacts();
        Assertions.assertEquals(phoneBookList, phoneBookListResp);
    }

    @Test
    public void saveContactsTestWhenInvalidPhoneNumber() {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.setName("MockName");
        phoneBook.setPhoneNumber("InvalidPhoneNumber");
        Exception e = Assertions.assertThrows(PhoneBookServiceException.class, ()->phoneBookService.saveContact(phoneBook));
        Assertions.assertEquals(e.getMessage(), Constants.INVALID_PHONE_FORMAT);
    }

    @Test
    public void saveContactsTestWhenExistingPhoneNumber() {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.setName("MockName");
        phoneBook.setPhoneNumber("123456789");
        List<PhoneBook> phoneBookList = new ArrayList<>();
        phoneBookList.add(phoneBook);
        Mockito.when(phoneBookRepository.findByPhoneNumber("123456789")).thenReturn(phoneBookList);
        Exception e = Assertions.assertThrows(PhoneBookServiceException.class, ()->phoneBookService.saveContact(phoneBook));
        Assertions.assertEquals(e.getMessage(), Constants.PHONE_ALREADY_EXISTS);
    }

    @Test
    public void saveContactsTestWhenInvalidName() {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.setName(null);
        phoneBook.setPhoneNumber("123456789");
        Mockito.when(phoneBookRepository.findByPhoneNumber("123456789")).thenReturn(null);
        Exception e = Assertions.assertThrows(PhoneBookServiceException.class, ()->phoneBookService.saveContact(phoneBook));
        Assertions.assertEquals(e.getMessage(), Constants.INVALID_NAME);
    }

    @Test
    public void saveContactsTestWhenExistingName() {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.setName("ExistingName");
        phoneBook.setPhoneNumber("123456789");
        List<PhoneBook> phoneBookList = new ArrayList<>();
        phoneBookList.add(phoneBook);
        Mockito.when(phoneBookRepository.findByPhoneNumber("123456789")).thenReturn(null);
        Mockito.when(phoneBookRepository.findByNameInIgnoreCase("ExistingName")).thenReturn(phoneBookList);
        Exception e = Assertions.assertThrows(PhoneBookServiceException.class, ()->phoneBookService.saveContact(phoneBook));
        Assertions.assertEquals(e.getMessage(), Constants.NAME_ALREADY_EXISTS);
    }

    @Test
    public void saveContactsTest() {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.setName("User");
        phoneBook.setPhoneNumber("123456789");
        Mockito.when(phoneBookRepository.findByPhoneNumber("123456789")).thenReturn(null);
        Mockito.when(phoneBookRepository.findByNameInIgnoreCase("ExistingName")).thenReturn(null);
        Mockito.when(phoneBookRepository.save(phoneBook)).thenReturn(phoneBook);
        ResponseEntity<String> resp = phoneBookService.saveContact(phoneBook);
        Assertions.assertEquals(resp.getBody(), "User saved successfully");
    }

    @Test
    public void getUniqueContactsTest() {
        PhoneBook phoneBook1 = new PhoneBook(1,"ExistingUser1", "123456781");
        PhoneBook phoneBook2 = new PhoneBook(2,"ExistingUser2", "123456782");
        PhoneBook phoneBook3 = new PhoneBook(3,"ExistingUser3", "123456783");
        PhoneBook phoneBook4 = new PhoneBook(4,"ExistingUser4", "123456784");
        List<PhoneBook> phoneBookList = new ArrayList<>();
        phoneBookList.add(phoneBook1);
        phoneBookList.add(phoneBook2);
        phoneBookList.add(phoneBook3);
        phoneBookList.add(phoneBook4);
        Mockito.when(phoneBookRepository.findAll()).thenReturn(phoneBookList);

        Set<String> newUniqueExpected = new TreeSet<String>();
        newUniqueExpected.add("NewUser5");
        Set<String> existingUniqueExpected = new TreeSet<String>();
        existingUniqueExpected.add("ExistingUser2");
        existingUniqueExpected.add("ExistingUser3");
        List<String> newUsers = Arrays.asList("ExistingUser1","existinguser4","NewUser5");
        Response response = phoneBookService.getUniqueContacts(newUsers);
        Assertions.assertEquals(response.getUniqueContacts().get("New"), newUniqueExpected);
        Assertions.assertEquals(response.getUniqueContacts().get("Existing"), existingUniqueExpected);
    }
}
