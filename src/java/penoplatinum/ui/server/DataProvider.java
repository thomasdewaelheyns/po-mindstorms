package penoplatinum.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

// class wrapping all logic to provide data from our datasource
public class DataProvider {
  private InitialContext context;
  private DataSource     ds;
  private Connection     con;
  private String         robot;

  // connects to the datasource
  protected DataProvider connect() {
    if( this.con != null ) { return this; } // we're already connected
    try {
      this.context = new InitialContext();
      this.ds      = (DataSource)context.lookup("java:db");
      this.con     = ds.getConnection();
    } catch(Exception e) {
      //this.sendError("Couldn't connect to DataSource.");
      throw new RuntimeException(e);
    }
    return this;
  }
  
  protected DataProvider forRobot(String robot) {
    this.robot = robot;
    return this;
  }

  // disconnects from the datasource
  protected DataProvider disconnect() {
    try {
      this.con.close();
    } catch( SQLException e ) {
      System.err.println( "Couldn't disconnect from DataSource" );
      throw new RuntimeException(e);
    }
    return this;
  }

  protected int getLastId(String source) {
    final String sql = "SELECT MAX(id) FROM " + source + " WHERE robot='" + this.robot + "';";
    try {
      ResultSet rs = this.con.createStatement().executeQuery(sql);
      if( rs.next() ) { 
        int id = rs.getInt(1);
        System.out.println( "Last " + source + ".id = " + id );
        return id;
      }
    } catch( Exception e ) {
      System.err.println( "Couldn't get last id... " + e );
    }
    return 0;
  }
  
  protected ResultSet getRS(String table, int lastId) throws java.sql.SQLException {
    return this.getRS(this.makeSql(table, lastId));
  }

  protected ResultSet getRS(String sql) throws java.sql.SQLException {
    return this.con.createStatement().executeQuery(sql);
  }

  private String makeSql(String table, int lastId) {
    return "SELECT * FROM " + table + " WHERE id > " + lastId + 
                                      " AND robot='" + this.robot + "';";
  }
  
}
