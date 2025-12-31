package com.example.lab10.projection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "account_views")
public class AccountViewDocument {
    @Id
    public String accountId;
    public String owner;
    public BigDecimal balance;

    public AccountViewDocument(String accountId, String owner, BigDecimal balance) {
        this.accountId = accountId;
        this.owner = owner;
        this.balance = balance;
    }
}