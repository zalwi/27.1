package pl.zalwi;

import pl.zalwi.data.Transaction;
import pl.zalwi.data.TransactionType;
import pl.zalwi.logic.TransactionDao;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class MainDaoTest {
    public static void main(String[] args) {
        Transaction transaction = new Transaction(1L, TransactionType.EXPENSE, "Piłka do nogi", new BigDecimal("159.99"),
                ZonedDateTime.of(2020, 7, 1, 10, 23, 15, 0, ZoneId.systemDefault()
                ));

        TransactionDao transactionDao = new TransactionDao();
        // create
//        transactionDao.create(transaction);

        // read
//        Optional<Transaction> optionalTransaction = transactionDao.read(1);
//        if(optionalTransaction.isPresent()){
//            System.out.println(optionalTransaction.get());
//        }

        //update
//        transactionDao.update(transaction);
        //delete
        transactionDao.delete(1);

        transactionDao.close();

    }
}
