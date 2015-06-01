package org.executequery.sql;

import org.executequery.GUIUtilities;
import org.executequery.actions.OpenFrameCommand;
import org.executequery.gui.ExecuteSqlScriptPanel;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Roman.Cherepovich on 31.05.2015.
 */
public class ScriptExecutor extends OpenFrameCommand {

  public static final String DEFAULT_SET_TERM = "SET TERM";
  public static final String DEFAULT_TERM = ";";

  private String setTermCommand = DEFAULT_SET_TERM;
  private String term = DEFAULT_TERM;

  public ScriptExecutor() {
  }

  public ScriptExecutor(String defaultTerm, String setTermCommand) {
    this.term = defaultTerm;
    this.setTermCommand = setTermCommand;
  }

  public void executeScript(Connection connection, Reader in)
      throws SQLException, IOException
  {
    StringBuffer sb = new StringBuffer();

    Statement stmt = connection.createStatement();
    try {
      int ch = 0;
      while ((ch = in.read()) != -1) {
        sb.append((char)ch);

        String possibleTerm =
            sb.substring(sb.length() - term.length());

        if (possibleTerm.equals(term)) {
          String command =
              sb.substring(0, sb.length() - term.length()).trim();

          processCommand(stmt, command);
        }
      }
    } catch (Exception e){
      GUIUtilities.displayErrorMessage(e);
    }
    finally {
      stmt.close();
    }
  }

  protected void processCommand(Statement stmt, String command)
      throws SQLException, IOException
  {
    if (command.toUpperCase().startsWith(setTermCommand)) {
      term = command.substring(setTermCommand.length()).trim();
    } else
      stmt.execute(command);
  }
}
