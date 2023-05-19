package org.example;

import org.example.dao.AccountDao;
import org.example.dao.AccountDaoJpaImpl;
import org.example.models.Account;

import javax.persistence.EntityManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AccountDao dao = new AccountDaoJpaImpl();


        List<Account> acc = dao.findAll();
        if (!acc.isEmpty()) {
            System.out.println("accounts:");
            acc.forEach(System.out::println);
        }else{
            Account b = new Account("Kama", "Choroszcz", 2L);
            Account c = new Account("Bober", "Storczykowa3", 3L);
            Account d = new Account("Chmielu", "Zakatek3", 4L);

            dao.save(b);
            dao.save(c);
            dao.save(d);

        }





    }
}