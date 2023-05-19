package org.example.models;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="\"ACCOUNT\"") // konfilkt ze slowem kluczowym SQL w H2 w wersjach 2.x., wymagane escapowanie
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long ID;
    private String name;
    private String address;
    private BigDecimal balance;


    public Account() { }

    public Account(String name, String address, Long id) {
        this.address = address;
        this.name = name;
        this.balance = BigDecimal.valueOf(0);
        this.ID = id;
    }

    public long getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                '}';
    }
}