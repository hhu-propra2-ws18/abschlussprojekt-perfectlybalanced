package de.hhu.abschlussprojektverleihplattform.database;

import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.*;
import java.util.Date;

public class DBUtils {
    public static PreparedStatementCreator psc(String sql, Object... objects){
        PreparedStatementCreator mypsc= new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //set the objects here
                mapParams(ps,objects);

                return ps;
            }
        };

        return mypsc;
    }

    //source: https://stackoverflow.com/questions/11777103/set-parameters-dynamically-to-prepared-statement-in-jdbc
    public static void mapParams(PreparedStatement ps, Object... args) throws SQLException {
        int i = 1;
        for (Object arg : args) {
            if (arg instanceof Date) {
                ps.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
            } else if (arg instanceof Integer) {
                ps.setInt(i++, (Integer) arg);
            } else if (arg instanceof Long) {
                ps.setLong(i++, (Long) arg);
            } else if (arg instanceof Double) {
                ps.setDouble(i++, (Double) arg);
            } else if (arg instanceof Float) {
                ps.setFloat(i++, (Float) arg);
            } else {
                ps.setString(i++, (String) arg);
            }
        }

        System.out.println(ps.toString());
    }
}
