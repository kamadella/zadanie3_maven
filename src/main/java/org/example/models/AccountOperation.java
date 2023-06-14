package org.example.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class AccountOperation extends AbstractModel{

    private BigDecimal amount;
    private OperationType type;

    public AccountOperation() {
    }

    public AccountOperation(Account account, BigDecimal amount, OperationType type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
    }

    @ManyToOne
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

}
