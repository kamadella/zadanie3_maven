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
    public BankImpl() {
        try {
            fh = new FileHandler("D:/PB/6semestr/java/zadanie2/zadanie2/log/MyLogFile.log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //dao = new AccountDaoJpaImpl();
        logger.addHandler(fh);
        logger.setLevel(Level.FINER);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        //listAccount = dao.findAll();
//        ID = getLastId();
        logger.info("utworzenie instancji banku");

    }

    public long ID = 0;
    private static Logger logger = Logger.getLogger(BankImpl.class.getName());
    //public AccountDao dao;
    private FileHandler fh = null;
    public List<Account> listAccount = new ArrayList<>();
//    public Long getLastId(){
//        return listAccount.isEmpty() ? 0 : listAccount.get(listAccount.size() - 1).getID();
//
//    }
    @Override
    public Long createAccount(String name, String address) {

        for (Account account: listAccount) {
            if (account.getName().equals(name) && account.getAddress().equals(address)){
                logger.fine("createAccount return: " + name);
                return account.getID();
            }
        }

        ID++;
        Account acc =new Account(name, address, ID);
        listAccount.add(acc);
        //dao.save(acc);
        logger.fine("createAccount create: " + name);
        return ID;
    }

    @Override
    public Long findAccount(String name, String address) {

        for (Account account: listAccount) {
            if (account.getName().equals(name) && account.getAddress().equals(address)){
                logger.fine("findAccount: name" + name );
                return account.getID();
            }
        }
        logger.fine("findAccount: null");
        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        if (id == null || listAccount.size() < id) {
            logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }

        else{
            for (Account account: listAccount) {
                Long acc = account.getID();
                if (acc.equals(id)){

                    logger.fine("deposit id: " + id + " amount: " + amount);
                    account.setBalance(account.getBalance().add(amount));
                }

            }
        }
    }

    @Override
    public BigDecimal getBalance(Long id) {

        if (id == null || listAccount.size() < id) {
            logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }
        else {
            for (Account account : listAccount) {
                if (account.getID() == id) {
                    logger.fine("getBalance id: " + id);
                    return account.getBalance();
                }
            }
        }
        return null;
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {

        if (id == null || listAccount.size() < id) {
            logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }
        else {
            for (Account account: listAccount) {
                if (account.getID() == id){
                    if (account.getBalance().compareTo(amount) < 0) {
                        logger.log(Level.SEVERE, "InsufficientFundsException" );
                        throw new InsufficientFundsException();
                    }
                    else {
                        logger.fine("withdraw id: " + id);
                        account.setBalance(account.getBalance().subtract(amount));
                    }
                }
            }
        }
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {

        Account accountSource = new Account();
        Account accountDestination = new Account();

        if (idSource == null || listAccount.size() < idSource || idDestination == null || listAccount.size() < idDestination) {
            logger.log(Level.SEVERE, "AccountIdException" );
            throw new AccountIdException();
        }

        else {
            for (Account account: listAccount) {
                if (account.getID() == idSource){
                    accountSource = account;
                }
                else if (account.getID() == idDestination){
                    accountDestination = account;
                }
            }
        }

        withdraw(accountSource.getID(), amount);
        deposit(accountDestination.getID(), amount);
        logger.fine("transfer Source: " + idSource + " Destination: " +idDestination);
    }
}
