package pl.zalwi.data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private Long id;                            // - automatycznie zwiększający się identyfikator rekordu
    private TransactionType type;               // - typ wpisu - wydatek lub przychód
    private String description;                 // - opis transakcji
    private BigDecimal amount;                  // - kwota transakcji
    private ZonedDateTime zonedDateTime;         // - data transakcji

    public Transaction(TransactionType type, String description, BigDecimal amount, ZonedDateTime zonedDateTime) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.zonedDateTime = zonedDateTime;
    }

    public Transaction(Long id, TransactionType type, String description, BigDecimal amount, ZonedDateTime zonedDateTime) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.zonedDateTime = zonedDateTime;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public String sqlDateTimeFormat(){
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String htmlDateTimeFormat(){
        return  zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + "T" +
                zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", zonedDateTime=" + zonedDateTime +
                '}';
    }
}
