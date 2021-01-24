package com.phonebook.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Data
public class Response {
    Map<String, Set<String>> uniqueContacts = new HashMap<>();
}
