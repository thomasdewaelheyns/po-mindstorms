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

// class wrapping all logic to stream a set of data ...
public class Streamer {
  private InitialContext context;
  private DataSource     ds;
  private Connection     con;
  private ResultSet      rs;
  private Integer        last = 0;

  public Streamer fastForward() {
    if( this.connect() ) {
      this.last = this.getLastId();
    }
    return this;
  }
  
  public Streamer rewind() {
    this.last = 0;
    return this;
  }

  public void service( HttpServletRequest request,
                       HttpServletResponse response ) 
                       throws ServletException, IOException 
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    // try to connect to the database
    if( ! this.connect() ) {
      out.println( "<script>parent.Dashboard.error('failed to connect to DS');</script>" );
      return;
    }

    try {
      System.out.println( "Starting stream..." );
      out.println( "<script>parent.Dashboard.start();</script>" );
      out.flush();

      // while we don't encounter any errors writing to the client...
      while( ! out.checkError() ) {
        this.getNextBatch();
        int id = -1, lightValue, barcode, sonarAngle, sonarDistance, rate;
        String ts, robot, lightColor, pushLeft, pushRight, event, source,
          plan, queue, action, argument;
        while(this.rs.next()) {
          id            = rs.getInt(1);
          ts            = rs.getString(2);
          robot         = rs.getString(3);
          lightValue    = rs.getInt(4);
          lightColor    = rs.getString(5);
          barcode       = rs.getInt(6);
          sonarAngle    = rs.getInt(7);
          sonarDistance = rs.getInt(8);
          pushLeft      = rs.getBoolean(9)  ? "true" : "false";
          pushRight     = rs.getBoolean(10) ? "true" : "false";
          event         = rs.getString(11);
          source        = rs.getString(12);
          plan          = rs.getString(13);
          queue         = rs.getString(14);
          action        = rs.getString(15);
          argument      = rs.getString(16);
          rate          = rs.getInt(17);

          out.println( "<script>parent.Dashboard.update( " +
            "'" + ts + "','" + robot + "'," +
            lightValue + ",'" + lightColor + "'," + barcode + "," +
            sonarAngle + "," + sonarDistance + "," +
            pushLeft + "," + pushRight + "," +
            "'" + event + "','" + source + "'," +
            "'" + plan + "','" + queue + "'," +
            "'" + action + "','" + argument + "'," +
            rate + ");</script>\n" );
        }
        out.print( " " ); // force output, causing exception when closed
        out.flush();      // flush to the browser for optimal UI experience
        this.rs.close();  // close this recordset
        if( id > this.last ) { this.last = id; } // keep track last sent id
        Thread.sleep(30);  // and breath ...
      }
    } catch( Exception e ) {
      // nothing todo, it's in fact normal ending when client disconnects
    } finally {
      // after not exposing the exception, clean up
      System.out.println( "Closing Stream..." );
      out.close();
      this.disconnect();
    }
  }

  // connects to the datasource
  private Boolean connect() {
    if( this.con != null ) { return true; }
    try {
      this.context = new InitialContext();
      this.ds      = (DataSource)context.lookup("java:db");
      this.con     = ds.getConnection();
    } catch(Exception e) {
      System.out.println( "Couldn't connect to DataSource" );
      return false;
    }
    return true;
  }

  // disconnects from the datasource
  private void disconnect() {
    try {
      this.con.close();
    } catch( SQLException e ) {
      System.out.println( "Couldn't disconnect from DataSource" );
    }
  }

  private void getNextBatch() throws NamingException, SQLException {
    final String sql = "SELECT * FROM logs WHERE id > " + this.last + ";";
    this.rs = this.con.createStatement().executeQuery(sql);
  }

  private int getLastId() {
    final String sql = "SELECT MAX(id) FROM logs;";
    try {
      this.rs = this.con.createStatement().executeQuery(sql);
      if( this.rs.next() ) { return rs.getInt(1) - 2; }
    } catch( Exception e ) {
      System.err.println( "Couldn't get last id... " + e );
    }
    return 0;
  }
}
