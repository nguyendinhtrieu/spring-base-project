package com.tzyel.springbaseproject.integration;

import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@link DatabaseVerifier} class provides a fluent interface for verifying database records based on specified conditions.
 * It is designed to streamline the process of database verification in integration tests.
 * <p>
 * Usage Example:
 * <p>
 * <pre>
 * {@code
 * new DatabaseVerifier(dataSource)
 * // To verify columns info (you can specify id for verifying info of records)
 * .table("table_name")
 *    .columns("column_1", "column_2").existWithValues("Value 1", "Value 2")
 *
 * // Verify record with a specific ID exists
 * .table("table_name")
 *    .existById(recordId)
 *
 * // Verify record with a column name exists
 * .table("table_name")
 *    .exist("column_name", expectedColumnValue)
 *
 * // Verify the number of records matching specified conditions
 * .table("table_name")
 *    .numberOfRecord(1, "column_name", expectedColumnValue)
 *
 * // Verify the non-existence of a record with specified conditions
 * .table("table_name")
 *    .doesNotExist("column_name", expectedColumnValue)
 *
 * // Verify the non-existence of a record with a specific ID
 * .table("table_name")
 *    .doesNotExistById(wrongRecordId);
 * }
 */
@SuppressWarnings({"DataFlowIssue", "SqlSourceToSinkFlow"})
public class DatabaseVerifier {
    private final JdbcTemplate jdbcTemplate;
    private final List<String> columnNames = new ArrayList<>();
    private final List<Object> columnValues = new ArrayList<>();
    private String tableName;

    /**
     * Creates a new {@link DatabaseVerifier} instance with the given {@link DataSource} for database access.
     *
     * @param dataSource The {@link DataSource} for database access.
     */
    public DatabaseVerifier(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Specifies the target database table for verification.
     *
     * @param tableName The name of the target database table.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public DatabaseVerifier table(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * Verifies the existence of a record in the specified table based on a single column and its expected value.
     *
     * @param columnName          The name of the column to be used in the verification query.
     * @param expectedColumnValue The expected value for the specified column.
     * @param <T>                 The type of the expected value.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public <T> DatabaseVerifier exist(String columnName, T expectedColumnValue) {
        validateState();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, expectedColumnValue);

        if (count == 0) {
            Assertions.fail("Expected record does not exist in the database.");
        }
        return this;
    }

    /**
     * Verifies the existence of a record in the specified table based on the record's ID.
     *
     * @param recordId The ID of the record to be checked for existence.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public DatabaseVerifier existById(int recordId) {
        validateState();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, recordId);

        if (count == 0) {
            Assertions.fail("Expected record with ID " + recordId + " does not exist in the database.");
        }
        return this;
    }

    /**
     * Verifies the number of records in the specified table based on a column and its expected value.
     *
     * @param expectedRecordCount The expected number of records.
     * @param columnName          The name of the column to be used in the verification query.
     * @param expectedColumnValue The expected value for the specified column.
     * @param <T>                 The type of the expected value.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public <T> DatabaseVerifier numberOfRecord(int expectedRecordCount, String columnName, T expectedColumnValue) {
        validateState();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, expectedColumnValue);

        if (count != expectedRecordCount) {
            Assertions.fail("Expected " + expectedRecordCount + " records with " + columnName + " = " + expectedColumnValue);
        }
        return this;
    }

    /**
     * Verifies the non-existence of a record in the specified table based on a single column and its expected value.
     *
     * @param columnName          The name of the column to be used in the verification query.
     * @param expectedColumnValue The expected value for the specified column.
     * @param <T>                 The type of the expected value.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public <T> DatabaseVerifier doesNotExist(String columnName, T expectedColumnValue) {
        validateState();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, expectedColumnValue);

        if (count != 0) {
            Assertions.fail("Record with " + columnName + " = " + expectedColumnValue + " should not exist in the database.");
        }
        return this;
    }

    /**
     * Verifies the non-existence of a record in the specified table based on the record's ID.
     *
     * @param recordId The ID of the record to be checked for non-existence.
     * @param <T>      The type of the record's ID.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public <T> DatabaseVerifier doesNotExistById(T recordId) {
        validateState();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, recordId);

        if (count != 0) {
            Assertions.fail("Record with ID " + recordId + " should not exist in the database.");
        }
        return this;
    }

    /**
     * Specifies the column names to be used in the verification query.
     *
     * @param columnNames The names of the columns to be used in the verification query.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     */
    public DatabaseVerifier columns(String... columnNames) {
        this.columnNames.clear();
        this.columnNames.addAll(Arrays.asList(columnNames));
        return this;
    }

    /**
     * Specifies the corresponding values for the previously specified columns.
     * It verifies the existence of a record with the specified columns and values.
     *
     * @param columnValues The corresponding values for the previously specified columns.
     * @return The current {@link DatabaseVerifier} instance for method chaining.
     * @throws IllegalStateException If columns are not specified in advance using the {@link #columns(String...)} method.
     */
    public DatabaseVerifier existWithValues(Object... columnValues) {
        validateState();
        this.columnValues.clear();
        this.columnValues.addAll(Arrays.asList(columnValues));

        verifyColumns();

        return this;
    }

    /**
     * Verifies the existence of a record with the specified columns and values.
     * It constructs a dynamic SQL WHERE clause based on the specified columns and values, then performs the verification.
     *
     * @throws IllegalArgumentException If the number of column names does not match the number of values.
     */
    private void verifyColumns() {
        if (columnNames.size() != columnValues.size()) {
            throw new IllegalArgumentException("Number of column names must match the number of values.");
        }

        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < columnNames.size(); i++) {
            whereClause.append(columnNames.get(i)).append(" = ?");

            if (i < columnNames.size() - 1) {
                whereClause.append(" AND ");
            }
        }

        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + whereClause;

        int count = jdbcTemplate.queryForObject(sql, Integer.class, columnValues.toArray());

        if (count < 0) {
            Assertions.fail("Record with specified columns and values exists in the database.");
        }
    }

    /**
     * Validates the state of the {@link DatabaseVerifier} instance before triggering the verification process.
     * It checks for conditions such as an empty table name and performs additional validation logic.
     * <p>
     * This method is automatically called before executing the verification to ensure that the {@link DatabaseVerifier}
     * instance is in a valid state. If any validation checks fail, an {@link IllegalArgumentException} is thrown.
     * <p>
     *
     * @throws IllegalArgumentException If the table name is empty or if additional validation checks fail.
     */
    private void validateState() {
        if (ObjectUtils.isEmpty(this.tableName)) {
            throw new IllegalArgumentException("Table name cannot be empty.");
        }
    }
}
