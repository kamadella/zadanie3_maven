package org.example.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="\"ACCOUNT\"") // konfilkt ze slowem kluczowym SQL w H2 w wersjach 2.x., wymagane escapowanie
public class Account extends AbstractModel{

    @Column(unique = true)
    private String name;
    private String address;
    private BigDecimal balance;

    @OneToMany(mappedBy = "account", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AccountOperation> accountOperation = new LinkedList<>();

    public Account() { }

    public Account(Long id, String name, String address) {
        this.name = name;
        this.address = address;
        this.balance = BigDecimal.valueOf(0);
    }

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress(){
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    public void setAccountOperation(List<AccountOperation> userGroups) {
        this.accountOperation = accountOperation;
    }
    public List<AccountOperation> getAccountOperation() {
        return accountOperation;
    }

    public void addOperation(BigDecimal amount, OperationType type) {
        // sprawdzic czy już nie jest dodana
        accountOperation.add(new AccountOperation(this, amount, type));
    }


    @Override
    public String toString() {
        return "Account{" +
                "ID=" + getId() +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                '}';
    }
}