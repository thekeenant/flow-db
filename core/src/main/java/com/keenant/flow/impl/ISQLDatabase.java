package com.keenant.flow.impl;

import com.keenant.flow.*;
import com.keenant.flow.exception.DatabaseException;
import com.keenant.flow.jdbc.QueryConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ISQLDatabase implements DatabaseContext {
    private final SQLDialect dialect;
    private final Connector connector;

    public ISQLDatabase(SQLDialect dialect, Connector connector) {
        this.dialect = dialect;
        this.connector = connector;
    }

    @Override
    public Query prepareQuery(String sql, List<Object> params, QueryConfig config) {
        try {
            PreparedStatement statement = connector.acquire().prepareStatement(sql, config.getType().getValue(), config.getConcurrency().getValue());
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            // Create the query object, passing on the query config to it
            return new IQuery(statement, config);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public SelectScoped selectFrom(Exp table) {
        return new ISelectScoped(table, this, dialect);
    }

    @Override
    public InsertScoped insertInto(Exp table) {
        return new IInsertScoped(table, this, dialect).newRecord();
    }

    @Override
    public void close() {
        connector.disposeAll();
    }
}
