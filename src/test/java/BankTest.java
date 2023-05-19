import org.example.dao.AccountDao;
import org.example.dao.AccountDaoJpaImpl;
import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.BankImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.Optional;

public class BankTest {

    private AccountDao accountDao;
    private BankImpl bank;

    @BeforeEach
    public void setup() {
        bank = new BankImpl();
        accountDao = new AccountDaoJpaImpl();
    }

    @AfterEach
    public void clear() {
        //  usuwa uzytkownikow po kazdym tescie
        for (Account u: accountDao.findAll())
            accountDao.delete(u);
    }

//    @Test
//    public void testDao() {
//
//        ;
//
//    }


//    @Test
//    public void testCreate() {
//        BankImpl bank = new BankImpl();
//        assert bank.createAccount( "nazwa", "adres") == 1;
//        assert bank.createAccount( "nazwa", "adres") == 1;
//        assert bank.createAccount( "nazwa1", "adres2") == 2;
//    }

    @Test
    public void testFind(){
        bank.createAccount( "nazwa", "adres");
        bank.createAccount( "nazwa1", "adres2");
        bank.createAccount( "DDD", "AAA");
        bank.createAccount( "sss", "sssss");
        bank.createAccount( "aaa", "fff");
        for (Account acc: bank.listAccount){
            accountDao.save(acc);
        }

        accountDao.findAll();
        assert bank.findAccount("nazwa", "adres") == 1;
        assert bank.findAccount("aaa", "fff") == 5;
        assert bank.findAccount("nazwa3", "adres333") == null;

    }

    @Test
    public void testDeposit(){
        //BankImpl bank = new BankImpl();

        bank.createAccount( "nazwa", "adres");
        bank.createAccount( "nazwa2", "adres2");

        for (Account acc: bank.listAccount){
            accountDao.save(acc);
        }

        accountDao.findAll();
        bank.deposit(1L, BigDecimal.valueOf(30));
        bank.deposit(1L, BigDecimal.valueOf(100));
        bank.deposit(2L, BigDecimal.valueOf(40));
        Optional<Account> acc = accountDao.findById(1L);
        //accountDao.update(acc);

        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.deposit(3L,  BigDecimal.valueOf(200));});

        assert bank.getBalance(1L).compareTo(BigDecimal.valueOf(130)) == 0;
        assert bank.getBalance(2L).compareTo(BigDecimal.valueOf(40)) == 0;

        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.getBalance(3L);});
    }


    @Test
    public void testWithdraw(){
        BankImpl bank = new BankImpl();
        bank.createAccount( "nazwa", "adres");
        bank.createAccount( "nazwa2", "adres2"); //2L
        bank.deposit(2L, BigDecimal.valueOf(100));
        bank.withdraw(2L, BigDecimal.valueOf(25) );

        assert bank.getBalance(2L).compareTo(BigDecimal.valueOf(75)) == 0;
        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.withdraw(3L,  BigDecimal.valueOf(200));});
        Assertions.assertThrows(Bank.InsufficientFundsException.class, ()-> {bank.withdraw(2L,  BigDecimal.valueOf(200));});

    }

    @Test
    public void testTransfer(){
        BankImpl bank = new BankImpl();
        bank.createAccount( "nazwa1", "adres1"); //1L
        bank.createAccount( "nazwa2", "adres2"); //2L
        bank.deposit(1L, BigDecimal.valueOf(100));
        bank.deposit(2L, BigDecimal.valueOf(100));
        bank.transfer(1L,2L, BigDecimal.valueOf(100));

        assert bank.getBalance(1L).compareTo(BigDecimal.valueOf(0)) == 0;
        assert bank.getBalance(2L).compareTo(BigDecimal.valueOf(200)) == 0;

        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.transfer(1L, 3L, BigDecimal.valueOf(100));});
        Assertions.assertThrows(Bank.InsufficientFundsException.class, ()-> {bank.transfer(1L, 2L, BigDecimal.valueOf(1000));});
    }

}
