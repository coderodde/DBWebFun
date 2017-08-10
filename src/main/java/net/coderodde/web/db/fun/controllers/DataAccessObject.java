package net.coderodde.web.db.fun.controllers;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.coderodde.web.db.fun.model.FunnyPerson;

public final class DataAccessObject {

    /**
     * For validating the email addresses.
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);

    /**
     * The SQL command for inserting a person.
     */
    private static final String INSERT_PERSON_SQL = 
            "INSERT INTO funny_persons (first_name, last_name, email) VALUES " +
            "(?, ?, ?);";

    /**
     * Creates a new database if not already created.
     */
    private static final String CREATE_DATABASE_SQL = 
            "CREATE DATABASE IF NOT EXISTS funny_db;";

    /**
     * Switches to 'funny_db'.
     */
    private static final String USE_DATABASE_SQL = "USE funny_db";

    /**
     * Creates the table if not already created.
     */
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS funny_persons (\n" +
                "id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,\n" +
                "first_name VARCHAR(40) NOT NULL,\n" +
                "last_name VARCHAR(40) NOT NULL,\n" +
                "email VARCHAR(50) NOT NULL,\n" +
                "created TIMESTAMP);";

    /**
     * The SQL for selecting a user given his/her ID.
     */
    private static final String GET_USER_BY_ID_SQL = 
            "SELECT * FROM funny_persons WHERE id = ?;";

    private final MysqlDataSource mysqlDataSource;

    private DataAccessObject(MysqlDataSource mysqlDataSource) {
        this.mysqlDataSource = Objects.requireNonNull(
                mysqlDataSource, 
                "The MysqlDataSource is null.");
    }

    private static final DataAccessObject INSTANCE;

    static {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");
        mysqlDataSource.setURL("jdbc:mysql://localhost:3306/funny_db");
        INSTANCE = new DataAccessObject(mysqlDataSource);
    }

    public static DataAccessObject instance() {
        return INSTANCE;
    }

    /**
     * Adds a person to the database.
     * 
     * @param person the person to add.
     */
    public void addPerson(FunnyPerson person) {
        checkPerson(person);

        try (Connection connection = mysqlDataSource.getConnection()) {
            try (PreparedStatement statement = 
                    connection.prepareStatement(INSERT_PERSON_SQL)) {
                statement.setString(1, person.getFirstName().trim());
                statement.setString(2, person.getLastName().trim());
                statement.setString(3, person.getEmail().trim());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates the empty database and the table.
     */
    public void createDatabase() {
        try (Connection connection = mysqlDataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(CREATE_DATABASE_SQL);
                statement.executeUpdate(USE_DATABASE_SQL);
                statement.executeUpdate(CREATE_TABLE_SQL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets a user by his/her ID.
     * 
     * @param id the ID of the user.
     * @return a {@code FunnyPerson} object or {@code null} if there is not such
     *         user.
     */
    public FunnyPerson getUserById(int id) {
        try (Connection connection = mysqlDataSource.getConnection()) {
            try (PreparedStatement statement = 
                    connection.prepareStatement(GET_USER_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    FunnyPerson person = new FunnyPerson();

                    person.setId(resultSet.getInt("id"));
                    person.setFirstName(resultSet.getString("first_name"));
                    person.setLastName(resultSet.getString("last_name"));
                    person.setEmail(resultSet.getString("email"));
                    person.setCreated(resultSet.getDate("created"));

                    return person;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void checkPerson(FunnyPerson person) {
        Objects.requireNonNull(person, "The person is null.");
        Objects.requireNonNull(person.getFirstName(), 
                               "The first name is null.");

        Objects.requireNonNull(person.getLastName(), "The last name is null.");
        Objects.requireNonNull(person.getEmail(), "The email is null.");

        if (person.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("The first name is empty.");
        }

        if (person.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("The last name is empty.");
        }

        if (!validate(person.getEmail().trim())) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    /**
     * Checks the email address.
     * 
     * @param email the email address to validate.
     * @return {@code true} if {@code email} is a valid email address.
     */
    private static boolean validate(String email ) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email );
        return matcher.find();
    }
}
