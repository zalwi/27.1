package pl.zalwi.logic;


import pl.zalwi.data.Transaction;
import pl.zalwi.data.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class TransactionDao {

    private static final String URL = "jdbc:mysql://localhost:3306/javastart?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";

    private static final String INSERT_QUERY = "INSERT INTO transactions(trans_type, trans_descr, amount, date_time) VALUES (?, ?, ?, ?)";
    private static final String SELECT_QUERY = "SELECT id, trans_type, trans_descr, amount, date_time FROM transactions WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE transactions SET trans_type = ?, trans_descr = ?, amount = ?, date_time = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM transactions WHERE id = ?";

    private Connection connection;

    public TransactionDao(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException exception) {
            System.err.println("Błąd podczas ładowania sterownika");
        } catch (SQLException exception) {
            System.err.println("Błąd podczas nawiązywania połączenia z bazą danych");
        }
    }

    //C - create
    public void create(Transaction transaction) {
        try {
            PreparedStatement createSql = connection.prepareStatement(INSERT_QUERY);
            createSql.setString(1, transaction.getType().toString());
            createSql.setString(2, transaction.getDescription());
            createSql.setBigDecimal(3, transaction.getAmount());
            createSql.setObject(4, transaction.getZonedDateTime().toLocalDateTime());
            createSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //R - read
    public Optional<Transaction> read(long id) {
        try {
            PreparedStatement selectSql = connection.prepareStatement(SELECT_QUERY);
            selectSql.setLong(1, id);

            ResultSet resultSet = selectSql.executeQuery();
            if (resultSet.next()) {
                Long idFromDatabase = resultSet.getLong("id");
                //trans_type, trans_descr, amount, datetime
                TransactionType transactionTypeFromDataBase = TransactionType.valueOf(resultSet.getString("trans_type"));
                String descriptionFromDatabase = resultSet.getString("trans_descr");
                BigDecimal amountFromDatabase = resultSet.getBigDecimal("amount");
                String tmpDateTime = resultSet.getString("date_time");
                ZonedDateTime zonedDateTimeFromDatabase = ZonedDateTime.parse(tmpDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Europe/Warsaw")));
                Transaction transaction = new Transaction(idFromDatabase, transactionTypeFromDataBase, descriptionFromDatabase, amountFromDatabase, zonedDateTimeFromDatabase);
                return Optional.of(transaction);
            } else {
                return Optional.empty();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }

    //U - update
    public void update(Transaction transaction) {
        try {
            PreparedStatement updateSql = connection.prepareStatement(UPDATE_QUERY);
            updateSql.setString(1, transaction.getType().toString());
            updateSql.setString(2, transaction.getDescription());
            updateSql.setBigDecimal(3, transaction.getAmount());
            updateSql.setObject(4, transaction.getZonedDateTime().toLocalDateTime());
            updateSql.setLong(5, transaction.getId());
            updateSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //D - delete
    public void delete(long id) {
        try {
            PreparedStatement deleteSql = connection.prepareStatement(DELETE_QUERY);
            deleteSql.setLong(1, id);
            deleteSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            System.err.println("Błąd podczas zamykania połączenia");
            exception.printStackTrace();
        }
    }
}
