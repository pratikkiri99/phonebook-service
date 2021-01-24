# PhoneBook microservice

## Overview

The application will allow user to save name and phone number into contact directory.
The user can also see list of contacts from the phone book. There is also an endpoint that compares two phonebook for unique records. 
    
###Details

The application starts on Port 8080  


#### URLS 

-Below url is to get list of all contacts.

GET http://localhost:8080/phonebook-service/api/contacts


-Below url is to save username and phone number

POST http://localhost:8080/phonebook-service/api/contacts/add

Request body in json format may contain below sample data.

{
    "name" : "Joe",
    "phoneNumber" : "789864123"
}

Swagger-Docs

http://localhost:8080/phonebook-service/v2/api-docs

Swagger-UI

http://localhost:8080/phonebook-service/swagger-ui.html

   
    