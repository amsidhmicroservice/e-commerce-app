package com.amsidh.mvc.customerservice.repository;

import com.amsidh.mvc.customerservice.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for Customer entity.
 * Provides CRUD operations for Customer documents in MongoDB.
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
