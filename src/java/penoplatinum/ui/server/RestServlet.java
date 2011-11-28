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
  private InitialContext context;
  private DataSource     ds;
  private Connection     con;
  private ResultSet      rs;

  private Integer last = 0;

  public void service( HttpServletRequest request,
                       HttpServletResponse response ) 
         throws ServletException, IOException 
  {
    PrintWriter out = response.getWriter();

    // try to connect to the database
    if( ! this.setupDS() ) {
      out.println( "error('failed to connect to DS');" );
      return;
    }
    
    try {
      out.println( "start(" + this.last + ");" );
      while(true) {
        this.getNextBatch();
        int id = -1; String ts, robot, message;
        while(this.rs.next()) {
          id      = rs.getInt(1);
          ts      = rs.getString(2);
          robot   = rs.getString(3);
          message = rs.getString(4);
          out.println( "update( '" + ts + "','" + message + "');" );
          out.flush();
        }
        this.rs.close();
        if( id > this.last ) { this.last = id; }
      }
    } catch( Exception e ) {
      // nothing todo, in fact normal ending when client disconnects
      out.println( "error(" + e.toString() + ");" );
    } finally {
      out.println( "stop(" + this.last + ");" );
      out.close();
    }
  }
  
  private Boolean setupDS() {
    try {
      this.context = new InitialContext();
      this.ds      = (DataSource)context.lookup("java:db");
      this.con     = ds.getConnection();
    } catch(Exception e) {
      return false;
    }
    return true;
  }
  
  private void getNextBatch() throws NamingException, SQLException {
    final String sql = "SELECT * FROM logs WHERE id > " + this.last + ";";
    this.rs = this.con.createStatement().executeQuery(sql);
  }
}
