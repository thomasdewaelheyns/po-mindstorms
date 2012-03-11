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
  private ResultSet      rsModel, rsWalls, rsAgents, rsWalls;
  private Integer        lastModel = 0 , lastWalls = 0, lastAgents = 0,
                         lastValues = 0;
  private PrintWriter    out;

  public Streamer fastForward() {
    if( this.connect() ) {
      this.lastModel  = this.getLastId("model");
      this.lastWalls  = this.getLastId("walls");
      this.lastAgents = this.getLastId("agents");
      this.lastValues = this.getLastId("values");
    }
    return this;
  }
  
  public Streamer rewind() {
    this.lastModel = this.lastWalls = this.lastAgents = this.lastValues = 0;
    return this;
  }

  public void service( HttpServletRequest request,
                       HttpServletResponse response ) 
                       throws ServletException, IOException 
  {
    response.setContentType("text/html");
    this.out = response.getWriter();

    this.connect();

    try {
      System.out.println( "Starting stream..." );
      this.sendUpdate(out.println( "<script>parent.Dashboard.start();</script>" );
      out.flush();

      // while we don't encounter any errors writing to the client...
      while( ! out.checkError() ) {
        this.sendModelUpdates();
        //this.sendWallUpdates();
        //this.sendValueUpdates();
        //this.sendAgentUpdates();
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
  private void connect() {
    if( this.con != null ) { return; } // we're already connected
    try {
      this.context = new InitialContext();
      this.ds      = (DataSource)context.lookup("java:db");
      this.con     = ds.getConnection();
    } catch(Exception e) {
      this.sendError("Couldn't connect to DataSource.");
      throw new RuntimeException(e);
    }
  }

  // disconnects from the datasource
  private void disconnect() {
    try {
      this.con.close();
    } catch( SQLException e ) {
      System.err.println( "Couldn't disconnect from DataSource" );
      throw new RuntimeException(e);
    }
  }

  // sends an error to the client
  private void sendError(String msg) {
    this.send( "error", msg );
  }
  
  // sends an method/msg combo to the client
  // we're hiding the details about the JS construction here
  private void send(String method, String msg) {
    this.out.println( "<script>parent.Dashboard." + method + "('" + msg + "');</script>" );    
  }
  
  private void sendModelUpdates() {
    final String sql = "SELECT * FROM model WHERE id > " + this.lastModel + ";";
    this.rsModel = this.con.createStatement().executeQuery(sql);

    int id = -1, lightValue, barcode, sonarAngle, sonarDistance, rate;
    String ts, robot, lightColor, pushLeft, pushRight, event, source,
           plan, queue, action, argument;
    while(this.rsModel.next()) {
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

      this.send( "update","'" + ts + "','" + robot + "'," +
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
    this.rsModel.close();  // close this recordset

    if( id > this.lastModel ) { this.lastModel = id; } // keep track last sent id
  }

  private int getLastId(String source) {
    final String sql = "SELECT MAX(id) FROM " + source + ";";
    try {
      this.rs = this.con.createStatement().executeQuery(sql);
      if( this.rs.next() ) { 
        int id = rs.getInt(1);
        System.out.println( "Last " + source + ".id = " + id );
        return id;
      }
    } catch( Exception e ) {
      System.err.println( "Couldn't get last id... " + e );
    }
    return 0;
  }
}


// HERE : TODO: reuse this.rs in genric way for all sequential connections


