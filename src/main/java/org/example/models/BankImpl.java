package org.example.models;

import com.sun.tools.javac.Main;
import org.example.dao.AccountDao;
import org.example.dao.AccountDaoJpaImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class BankImpl implements Bank{
    public BankImpl(AccountDao accountDao) {
        try {
            fh = new FileHandler("D:/PB/6semestr/java/zadanie2/zadanie2/log/MyLogFile.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dao = accountDao;
        logger.addHandler(fh);
        logger.setLevel(Level.FINER);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        listAccount = dao.findAll();
//      ID = getLastId();
        logger.info("utworzenie instancji banku");

    }

    public long ID = 0;
    private static Logger logger = Logger.getLogger(BankImpl.class.getName());
    public AccountDao dao;
    private FileHandler fh = null;
    public List<Account> listAccount;

//    public Long getLastId(){
//        return listAccount.isEmpty() ? 0 : listAccount.get(listAccount.size() - 1).getID();
//
//    }

    @Override
    public Long createAccount(String name, String address) {

        for (Account account: listAccount) {
            if (account.getName().equals(name) && account.getAddress().equals(address)){
                //logger.fine("createAccount return: " + name);
                return account.getID();
            }
        }

        ID++;
        Account acc = new Account(name, address, ID);
        listAccount.add(acc);
        dao.save(acc);
        //logger.fine("createAccount create: " + name);
        return ID;
    }

    @Override
    public Long findAccount(String name, String address) {

        for (Account account: listAccount) {
            if (account.getName().equals(name) && account.getAddress().equals(address)){
                //logger.fine("findAccount: name" + name );
                return account.getID();
            }
        }
        //logger.fine("findAccount: null");
        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        if (id == null || listAccount.size() < id) {
            //logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }

        else{
            Account a = dao.findById(id).get(); //znajduje konto o podanym id
            a.setBalance(a.getBalance().add(amount));
            dao.update(a);
        }
    }

    @Override
    public BigDecimal getBalance(Long id) {

        if (id == null || listAccount.size() < id) {
            //logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }
        else {
            Account a = dao.findById(id).get(); //znajduje konto o podanym id
            System.out.println("KONTO TU TU ->" + a);
            return a.getBalance();
        }
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {

        if (id == null || listAccount.size() < id) {
            //logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }
        else {
            Account a = dao.findById(id).get(); //znajduje konto o podanym id
            if (a.getBalance().compareTo(amount) < 0) {
                //logger.log(Level.SEVERE, "InsufficientFundsException" );
                throw new InsufficientFundsException();
            }
            else {
                a.setBalance(a.getBalance().subtract(amount));
                dao.update(a);
            }
        }
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {

        Account accountSource = new Account();
        Account accountDestination = new Account();

        if (idSource == null || listAccount.size() < idSource || idDestination == null || listAccount.size() < idDestination) {
            //logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }

        else {
            accountSource = dao.findById(idSource).get(); //znajduje konto o podanym id
            accountDestination = dao.findById(idDestination).get(); //znajduje konto o podanym id
        }

        withdraw(accountSource.getID(), amount);
        deposit(accountDestination.getID(), amount);
        //logger.fine("transfer Source: " + idSource + " Destination: " +idDestination);
    }
}
