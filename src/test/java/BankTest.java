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
        accountDao = new AccountDaoJpaImpl();
        bank = new BankImpl(accountDao);

    }

    @AfterEach
    public void clear() {
        //  usuwa uzytkownikow po kazdym tescie
        for (Account u: accountDao.findAll()){
            accountDao.delete(u);
        }

    }

    @Test
    public void testCreate() {
        //BankImpl bank = new BankImpl();
        assert bank.createAccount( "nazwa", "adres") != null;
        assert bank.createAccount( "nazwa", "adres")  != null;
        assert bank.createAccount( "nazwa1", "adres2")  != null;
    }

    @Test
    public void testFind(){
        //BankImpl bank = new BankImpl();
        bank.createAccount( "nazwa", "adres");
        bank.createAccount( "nazwa1", "adres2");
        bank.createAccount( "DDD", "AAA");
        bank.createAccount( "sss", "sssss");
        bank.createAccount( "aaa", "fff");
        assert bank.findAccount("nazwa", "adres") != null;
        assert bank.findAccount("aaa", "fff")  != null;
        assert bank.findAccount("nazwa3", "adres333") == null;

    }

    @Test
    public void testBalance(){
        Long id = bank.createAccount( "nazwa", "adres");
        System.out.println(id);
        assert bank.getBalance(id) == BigDecimal.valueOf(0);
    }

    @Test
    public void testDeposit(){
        //BankImpl bank = new BankImpl();

        Long id1 = bank.createAccount( "nazwa", "adres");
        Long id2 =bank.createAccount( "nazwa2", "adres2");

        bank.deposit(id1, BigDecimal.valueOf(30));
        bank.deposit(id1, BigDecimal.valueOf(100));
        bank.deposit(id2, BigDecimal.valueOf(40));

        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.deposit(null,  BigDecimal.valueOf(200));});

        assert bank.getBalance(id1).compareTo(BigDecimal.valueOf(130)) == 0;
        assert bank.getBalance(id2).compareTo(BigDecimal.valueOf(40)) == 0;

        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.getBalance(null);});
    }


    @Test
    public void testWithdraw(){
        //BankImpl bank = new BankImpl();
        Long id1 = bank.createAccount( "nazwa", "adres");
        Long id2 = bank.createAccount( "nazwa2", "adres2"); //2L
        bank.deposit(id2, BigDecimal.valueOf(100));
        bank.withdraw(id2, BigDecimal.valueOf(25) );

        assert bank.getBalance(id2).compareTo(BigDecimal.valueOf(75)) == 0;
        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.withdraw(null,  BigDecimal.valueOf(200));});
        Assertions.assertThrows(Bank.InsufficientFundsException.class, ()-> {bank.withdraw(id2,  BigDecimal.valueOf(200));});

    }

    @Test
    public void testTransfer(){
        //BankImpl bank = new BankImpl();
        Long id1 =bank.createAccount( "nazwa1", "adres1"); //1L
        Long id2 =bank.createAccount( "nazwa2", "adres2"); //2L
        bank.deposit(id1, BigDecimal.valueOf(100));
        bank.deposit(id2, BigDecimal.valueOf(100));
        bank.transfer(id1,id2, BigDecimal.valueOf(100));

        assert bank.getBalance(id1).compareTo(BigDecimal.valueOf(0)) == 0;
        assert bank.getBalance(id2).compareTo(BigDecimal.valueOf(200)) == 0;

        Assertions.assertThrows(Bank.AccountIdException.class, ()-> {bank.transfer(id1, null, BigDecimal.valueOf(100));});
        Assertions.assertThrows(Bank.InsufficientFundsException.class, ()-> {bank.transfer(id1, id2, BigDecimal.valueOf(1000));});
    }

}
