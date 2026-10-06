package su.nightexpress.engine.component;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.config.DatabaseType;
import su.nightexpress.nightcore.db.connection.AbstractConnector;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;
import su.nightexpress.nightcore.db.statement.template.UpdateStatement;
import su.nightexpress.nightcore.db.statement.type.BatchStatement;
import su.nightexpress.nightcore.db.table.Table;

@NullMarked
public interface DatabaseClient {

    DatabaseConfig getConfig();

    DatabaseType getDatabaseType();

    String getTablePrefix();

    AbstractConnector getConnector();

    <T> void addCustomSync(Table table, RowMapper<T> mapper, Consumer<T> consumer);

    void addTableSync(Table table, Consumer<ResultSet> consumer);

    void addTableSync(String tableName, Consumer<ResultSet> consumer);

    void createTable(Table table);

    void addColumn(Table table, Column<?> column);

    void renameColumn(Table table, Column<?> column, String targetName);

    void renameColumn(Table table, String sourceName, String targetName);

    void dropColumn(Table table, String... columnNames);

    boolean hasColumn(Table table, Column<?> column);


    <T> void insert(Table table, InsertStatement<T> statement, @NonNull T entity);

    <T> void insert(Table table, InsertStatement<T> statement,
                    Collection<T> entities);

    <T> void update(Table table, UpdateStatement<T> statement, @NonNull T entity);

    <T> void update(Table table, UpdateStatement<T> statement,
                    Collection<T> entities);

    <T> void update(Table table, UpdateStatement<T> statement, @NonNull T entity,
                    @Nullable Wheres<T> wheres);

    <T> void update(Table table, UpdateStatement<T> statement, Collection<T> entities,
                    @Nullable Wheres<T> wheres);

    void delete(Table table);

    void delete(Table table, @Nullable Wheres<Object> wheres);

    <T> void delete(Table table, @NonNull T entity);

    <T> void delete(Table table, @NonNull T entity, @Nullable Wheres<T> wheres);

    <T> void delete(Table table, Collection<T> entities);

    <T> void delete(Table table, Collection<T> entities, @Nullable Wheres<T> wheres);

    <T> void executeBatch(Table table, BatchStatement<T> query, @NonNull T entity,
                          @Nullable Wheres<T> wheres);

    <T> void executeBatch(String table, BatchStatement<T> query, @NonNull T entity,
                          @Nullable Wheres<T> wheres);

    <T> void executeBatch(Table table, BatchStatement<T> query,
                          Collection<T> entities, @Nullable Wheres<T> wheres);

    <T> void executeBatch(String table, BatchStatement<T> query,
                          Collection<T> entities, @Nullable Wheres<T> wheres);

    void executeStatement(String statement, @Nullable Wheres<Object> wheres);

    boolean contains(Table table, Wheres<Object> wheres);


    <R> Optional<R> selectAnyFirst(Table table, SelectStatement<R> query);


    <R> Optional<R> selectFirst(Table table, SelectStatement<R> query,
                                Wheres<Object> wheres);


    <R> List<R> selectAny(Table table, SelectStatement<R> query);


    <R> List<R> selectWhere(Table table, SelectStatement<R> query,
                            Wheres<Object> wheres);
}
