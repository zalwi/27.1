package pl.zalwi.data;

public enum TransactionType {
    EXPENSE("wydatek"),
    INCOME("przychód");

    private String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
