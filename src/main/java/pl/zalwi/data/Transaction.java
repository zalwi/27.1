package pl.zalwi.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private Long id;
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    private LocalDateTime localDateTime;

    public Transaction(TransactionType type, String description, BigDecimal amount, LocalDateTime localDateTime) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.localDateTime = localDateTime;
    }

    public Transaction(Long id, TransactionType type, String description, BigDecimal amount, LocalDateTime localDateTime) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.localDateTime = localDateTime;
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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getSqlDateTimeFormat() {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", zonedDateTime=" + localDateTime +
                '}';
    }
}
