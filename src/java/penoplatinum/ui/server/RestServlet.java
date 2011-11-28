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

@SuppressWarnings("serial")
public class RestServlet extends HttpServlet {

  public void service( HttpServletRequest request,
    HttpServletResponse response ) 
    throws ServletException, IOException 
  {
    new Streamer().service(request, response);
  }

  // class wrapping all logic to stream a set of data ...
  private class Streamer {
    private InitialContext context;
    private DataSource     ds;
    private Connection     con;
    private ResultSet      rs;
    private Integer        last = 0;

    public void service( HttpServletRequest request,
                         HttpServletResponse response ) 
           throws ServletException, IOException 
    {
      PrintWriter out = response.getWriter();

      // try to connect to the database
      if( ! this.connect() ) {
        out.println( "error('failed to connect to DS');" );
        return;
      }

      try {
        System.out.println( "Starting stream..." );
        out.println( "start();" );

        // while we don't encounter any errors writing to the client...
        while(!out.checkError()) {
          this.getNextBatch();
          int id = -1; String ts, robot, message;
          while(this.rs.next()) {
            id      = rs.getInt(1);
            ts      = rs.getString(2);
            robot   = rs.getString(3);
            message = rs.getString(4);
            out.println( "update( '" + ts + "','" + message + "');" );
          }
          out.print( " " ); // force output, causing exception when closed
          out.flush();      // flush to the browser for optimal UI experience
          this.rs.close();  // close this recordset
          if( id > this.last ) { this.last = id; } // keep track last sent id
          Thread.sleep(1);  // and breath
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
  }
}
