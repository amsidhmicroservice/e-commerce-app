package com.amsidh.mvc.customerservice.repository;

import com.amsidh.mvc.customerservice.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
