package org.executequery.sql.parser;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.firebirdsql.jdbc.parser.CaseInsensitiveStream;
import org.firebirdsql.jdbc.parser.JaybirdSqlLexer;
import org.firebirdsql.jdbc.parser.JaybirdSqlParser;
import org.firebirdsql.jdbc.parser.JaybirdStatementModel;

/**
 * Created by Roman.Cherepovich on 01.06.2015.
 */
public class StatementParserImpl implements StatementParser {
  @Override
  public JaybirdStatementModel parseInsertStatement(String sql) throws ParseException {
    try {
      CharStream stream = new CaseInsensitiveStream(sql);
      JaybirdSqlLexer lexer = new JaybirdSqlLexer(stream);
      CommonTokenStream tokenStream = new CommonTokenStream(lexer);

      JaybirdSqlParser parser = new JaybirdSqlParser(tokenStream);
      parser.statement().getTree();

      JaybirdStatementModel statementModel = parser.getStatementModel();
      return statementModel;
    } catch (RecognitionException e) {
      throw new ParseException("Unable to parse query", e);
    }
  }
}
