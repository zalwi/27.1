package pl.zalwi.data;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionsRepository {

    private List<Transaction> transactionList;

    public TransactionsRepository() {
        transactionList = new ArrayList<Transaction>();
        transactionList.add(new Transaction(    1L,
                                                    TransactionType.EXPENSE,
                                                    "Piłka do kosza",
                                                    new BigDecimal("59.99"),
                                                    ZonedDateTime.of(2020,7,13,10,23,15,0, ZoneId.systemDefault()
                                                    )));
        transactionList.add(new Transaction(    2L,
                                                    TransactionType.INCOME,
                                                    "Prezent urodzinowy",
                                                    new BigDecimal("200"),
                                                    ZonedDateTime.of(2020,7,13,17,10,56,0, ZoneId.systemDefault()
                                                    )));
        transactionList.add(new Transaction(    3L,
                                                    TransactionType.EXPENSE,
                                                    "Wędka",
                                                    new BigDecimal("359"),
                                                    ZonedDateTime.of(2020,7,9,8,45,42,0, ZoneId.systemDefault()
                                                    )));
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}
