package pl.zalwi.logic;

import org.springframework.stereotype.Service;
import pl.zalwi.data.Transaction;
import pl.zalwi.data.TransactionType;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionDao {

    private static final String URL = "jdbc:mysql://localhost:3306/javastart?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";

    private static final String INSERT_QUERY                = "INSERT " +
                                                                "INTO transactions(trans_type, trans_descr, amount, date_time) " +
                                                                "VALUES (?, ?, ?, ?)";
    private static final String SELECT_QUERY                = "SELECT id, trans_type, trans_descr, amount, date_time " +
                                                                "FROM transactions " +
                                                                "WHERE id = ?";
    private static final String UPDATE_QUERY                = "UPDATE transactions " +
                                                                "SET trans_type = ?, trans_descr = ?, amount = ?, date_time = ? " +
                                                                "WHERE id = ?";
    private static final String DELETE_QUERY                = "DELETE FROM transactions WHERE id = ?";
    private static final String SELECT_ALL_QUERY            = "SELECT * FROM transactions";
    private static final String SELECT_ALL_OF_TYPE_QUERY    = "SELECT * FROM transactions WHERE trans_type = ?";

    private Connection connection;

    public TransactionDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException exception) {
            System.err.println("Błąd podczas ładowania sterownika");
        } catch (SQLException exception) {
            System.err.println("Błąd podczas nawiązywania połączenia z bazą danych");
        }
    }

    public void create(Transaction transaction) {
        try {
            PreparedStatement createSql = connection.prepareStatement(INSERT_QUERY);
            createSql.setString(    1, transaction.getType().toString());
            createSql.setString(    2, transaction.getDescription());
            createSql.setBigDecimal(3, transaction.getAmount());
            createSql.setObject(    4, transaction.getLocalDateTime());
            createSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Optional<Transaction> read(long id) {
        try {
            PreparedStatement selectSql = connection.prepareStatement(SELECT_QUERY);
            selectSql.setLong(1, id);
            ResultSet resultSet = selectSql.executeQuery();
            if (resultSet.next()) {
                return Optional.of(convertToTransaction(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Transaction> findByCategory(TransactionType transactionType){
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        try {
            PreparedStatement selectSql = connection.prepareStatement(SELECT_ALL_OF_TYPE_QUERY);
            selectSql.setString(1, transactionType.toString());
            ResultSet resultSet = selectSql.executeQuery();
            while (resultSet.next()) {
                transactionsList.add(convertToTransaction(resultSet));
            }
            return transactionsList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return transactionsList;
    }

    public List<Transaction> findAll(){
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        try {
            PreparedStatement selectSql = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = selectSql.executeQuery();
            while (resultSet.next()) {
                transactionsList.add(convertToTransaction(resultSet));
            }
            return transactionsList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return transactionsList;
    }

    public void update(Transaction transaction) {
        try {
            PreparedStatement updateSql = connection.prepareStatement(UPDATE_QUERY);
            updateSql.setString(    1, transaction.getType().toString());
            updateSql.setString(    2, transaction.getDescription());
            updateSql.setBigDecimal(3, transaction.getAmount());
            updateSql.setObject(    4, transaction.getLocalDateTime());
            updateSql.setLong(      5, transaction.getId());
            updateSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void delete(long id) {
        try {
            PreparedStatement deleteSql = connection.prepareStatement(DELETE_QUERY);
            deleteSql.setLong(1, id);
            deleteSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private Transaction convertToTransaction(ResultSet resultSet){
        try {
            Long            idFromDatabase              = resultSet.getLong("id");
            TransactionType transactionTypeFromDataBase = TransactionType.valueOf(resultSet.getString("trans_type"));
            String          descriptionFromDatabase     = resultSet.getString("trans_descr");
            BigDecimal      amountFromDatabase          = resultSet.getBigDecimal("amount");
            LocalDateTime   localDateTimeFromDatabase   = LocalDateTime.parse(  resultSet.getString("date_time"),
                                                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new Transaction(idFromDatabase, transactionTypeFromDataBase, descriptionFromDatabase, amountFromDatabase, localDateTimeFromDatabase);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @PreDestroy
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            System.err.println("Błąd podczas zamykania połączenia");
            exception.printStackTrace();
        }
    }
}
