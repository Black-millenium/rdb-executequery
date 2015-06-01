package org.executequery.sql.parser;

import org.firebirdsql.jdbc.parser.JaybirdStatementModel;

/**
 * Created by Roman.Cherepovich on 01.06.2015.
 */
public interface StatementParser {

  /**
   * Parses the provided SQL into a statement model
   *
   * @param sql SQL query text to parse
   * @return Statementmodel
   * @throws StatementParser.ParseException For errors parsing the query
   */
  JaybirdStatementModel parseInsertStatement(String sql) throws ParseException;

  /**
   * Exception to wrap other exceptions when parsing.
   */
  public class ParseException extends Exception {

    private static final long serialVersionUID = 2440030356284907181L;

    public ParseException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
