package com.example.lab10.projection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountViewRepository extends MongoRepository<AccountViewDocument, String> {

}