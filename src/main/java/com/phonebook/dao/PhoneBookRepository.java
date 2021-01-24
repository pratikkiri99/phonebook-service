package com.phonebook.dao;

import com.phonebook.model.PhoneBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhoneBookRepository extends JpaRepository<PhoneBook, String> {

    @Query("select phonebook from PhoneBook phonebook where upper(name) = upper(:name)")
    List<PhoneBook> findByNameInIgnoreCase(String name);

    List<PhoneBook> findByPhoneNumber(String phoneNumber);
}
