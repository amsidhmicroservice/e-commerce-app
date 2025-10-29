package com.amsidh.mvc.customerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Customer entity representing a customer in the system.
 * Stored in MongoDB collection "customers".
 * Contains customer personal information and address details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "customers")
public class Customer implements Serializable {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;

}
