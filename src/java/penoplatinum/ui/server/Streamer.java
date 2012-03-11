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

import java.util.Map;
import java.util.HashMap;

// class wrapping all logic to stream a set of data ...
public class Streamer {
  private InitialContext context;
  private DataSource     ds;
  private Connection     con;
  private ResultSet      rs;
  private Integer        lastModel = 0 , lastWalls = 0, lastAgents = 0,
                         lastValues = 0;
  private PrintWriter    out;

  public Streamer fastForward() {
    this.connect();
    this.lastModel  = this.getLastId("model");
    this.lastWalls  = this.getLastId("sectorWalls");
    this.lastAgents = this.getLastId("sectorAgents");
    this.lastValues = this.getLastId("sectorvalues");
    return this;
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
      this.send( "start","" );
      out.flush();

      // while we don't encounter any errors writing to the client...
      while( ! out.checkError() ) {
        this.sendModelUpdates();
        this.sendWallUpdates();
        this.sendValueUpdates();
        this.sendAgentUpdates();
        Thread.sleep(30);  // and breath ...
      }
    } catch( Exception e ) {
      // nothing todo, it's in fact normal ending when client disconnects
      throw new RuntimeException(e);
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
    this.out.println( "<script>parent.Dashboard." + 
                      method + "(" + msg + ");</script>" );    
  }
  
  private String makeSql(String table, int lastId) {
    return "SELECT * FROM " + table + " WHERE id > " + lastId + ";";
  }

  private void getRS(String table, int lastId) throws java.sql.SQLException {
    this.rs = this.con.createStatement()
                  .executeQuery(this.makeSql(table, lastId));
  }
  
  private void sendModelUpdates() throws java.sql.SQLException {
    this.getRS("model", this.lastModel);

    int id = -1, lightValue, avgLightValue, barcode, sonarAngle, 
        ir1, ir2, ir3, ir4, ir5, ir_dist, 
        walls, value_n, value_e, value_s, value_w,
        sonarDistance, rate;
    String ts, robot, lightColor, pushLeft, pushRight, event, source,
           plan, queue, action, argument;

    while(this.rs.next()) {
      id            = rs.getInt(1);
      ts            = rs.getString(2);
      robot         = rs.getString(3);
      lightValue    = rs.getInt(4);
      lightColor    = rs.getString(5);
      avgLightValue = rs.getInt(6);
      barcode       = rs.getInt(7);
      sonarAngle    = rs.getInt(8);
      sonarDistance = rs.getInt(9);
      ir1           = rs.getInt(10);
      ir2           = rs.getInt(11);
      ir3           = rs.getInt(12);
      ir4           = rs.getInt(13);
      ir5           = rs.getInt(14);
      ir_dist       = rs.getInt(15);
      walls         = rs.getInt(16);
      value_n       = rs.getInt(17);
      value_e       = rs.getInt(18);
      value_s       = rs.getInt(19);
      value_w       = rs.getInt(20);
      event         = rs.getString(21);
      source        = rs.getString(22);
      plan          = rs.getString(23);
      queue         = rs.getString(24);
      action        = rs.getString(25);
      argument      = rs.getString(26);
      rate          = rs.getInt(27);

      this.send( "update","'" + ts + "','" + robot + "'," +
                 lightValue + ",'" + lightColor + "'," + avgLightValue + "," +
                 barcode + "," +
                 sonarAngle + "," + sonarDistance + "," +
                 ir1 + "," + ir2 + "," + ir3 + "," + ir4 + "," + ir5 + "," +
                 ir_dist + "," +
                 walls + "," + 
                 value_n + "," + value_e + "," + value_s + "," + value_w + "," +
                 "'" + event + "','" + source + "'," +
                 "'" + plan + "','" + queue + "'," +
                 "'" + action + "','" + argument + "'," + rate );
    }
    out.print( " " ); // force output, causing exception when closed
    out.flush();      // flush to the browser for optimal UI experience
    this.rs.close();  // close this recordset

    if( id > this.lastModel ) { this.lastModel = id; } // track last sent id
  }

  private void sendWallUpdates() throws java.sql.SQLException {
    this.getRS("sectorWalls", this.lastWalls);

    int id = -1, left, top, walls;
    String ts = "", robot = "", grid;

    // group the wall updates
    Map<String,Map<String,Integer>> map = 
      new HashMap<String,Map<String,Integer>>();
    while(this.rs.next()) {
      id    = rs.getInt(1);
      ts    = rs.getString(2);
      robot = rs.getString(3);
      grid  = rs.getString(4);
      left  = rs.getInt(5);
      top   = rs.getInt(6);
      walls = rs.getInt(7);

      if(!map.containsKey(grid)) {
        map.put(grid, new HashMap<String,Integer>());
      }
      map.get(grid).put(left+","+top, walls);
    }

    // send grouped updates for each grid
    for(Map.Entry<String,Map<String,Integer>> e : map.entrySet()) {
      grid = e.getKey();
      String updateString = "";
      Map<String,Integer> updates = e.getValue();
      boolean first = true;
      for(Map.Entry<String,Integer> w : updates.entrySet()) {
        if(first) { first = false; } else { updateString += ","; }
        updateString += "'" + w.getKey() + "' : " + w.getValue();
      }
      this.send( grid + ".updateWalls", "'" + ts + "','" + robot + "'," +
                 "{" + updateString + "}" );
    }
    this.out.print( " " ); // force output, causing exception when closed
    this.out.flush();      // flush to the browser for optimal UI experience
    this.rs.close();       // close this recordset

    if( id > this.lastWalls ) { this.lastWalls = id; } // track last sent id
  }

  private void sendValueUpdates() throws java.sql.SQLException {
    this.getRS("sectorValues", this.lastValues);

    int id = -1, left, top, value;
    String ts = "", robot = "", grid;

    // group the wall updates
    Map<String,Map<String,Integer>> map = 
      new HashMap<String,Map<String,Integer>>();
    while(this.rs.next()) {
      id    = rs.getInt(1);
      ts    = rs.getString(2);
      robot = rs.getString(3);
      grid  = rs.getString(4);
      left  = rs.getInt(5);
      top   = rs.getInt(6);
      value = rs.getInt(7);

      if(!map.containsKey(grid)) {
        map.put(grid, new HashMap<String,Integer>());
      }
      map.get(grid).put(left+","+top, value);
    }

    // send grouped updates for each grid
    for(Map.Entry<String,Map<String,Integer>> e : map.entrySet()) {
      grid = e.getKey();
      String updateString = "";
      Map<String,Integer> updates = e.getValue();
      boolean first = true;
      for(Map.Entry<String,Integer> w : updates.entrySet()) {
        if(first) { first = false; } else { updateString += ","; }
        updateString += "'" + w.getKey() + "' : " + w.getValue();
      }
      this.send( grid + ".updateValues", "'" + ts + "','" + robot + "'," +
                 "{" + updateString + "}" );
    }
    this.out.print( " " ); // force output, causing exception when closed
    this.out.flush();      // flush to the browser for optimal UI experience
    this.rs.close();       // close this recordset

    if( id > this.lastValues ) { this.lastValues = id; } // track last sent id
  }

  private void sendAgentUpdates() throws java.sql.SQLException {
    this.getRS("sectorAgents", this.lastAgents);

    int id = -1, left, top, bearing;
    String ts = "", robot = "", grid, name, color;

    // group the agent updates
    Map<String,Map<String,String>> map = 
      new HashMap<String,Map<String,String>>();
    while(this.rs.next()) {
      id      = rs.getInt(1);
      ts      = rs.getString(2);
      robot   = rs.getString(3);
      grid    = rs.getString(4);
      name    = rs.getString(5);
      left    = rs.getInt(6);
      top     = rs.getInt(7);
      bearing = rs.getInt(8);
      color   = rs.getString(9);

      if(!map.containsKey(grid)) {
        map.put(grid, new HashMap<String,String>());
      }
      map.get(grid).put(name, "left:"+left+",top:"+top+",color:'"+color+"'");
    }

    // send grouped updates for each grid
    for(Map.Entry<String,Map<String,String>> e : map.entrySet()) {
      grid = e.getKey();
      String updateString = "";
      Map<String,String> updates = e.getValue();
      boolean first = true;
      for(Map.Entry<String,String> w : updates.entrySet()) {
        if(first) { first = false; } else { updateString += ","; }
        updateString += "'" + w.getKey() + "' : { " + w.getValue() + "}";
      }
      this.send( grid + ".updateAgents", "'" + ts + "','" + robot + "'," +
                 "{" + updateString + "}" );
    }
    this.out.print( " " ); // force output, causing exception when closed
    this.out.flush();      // flush to the browser for optimal UI experience
    this.rs.close();       // close this recordset

    if( id > this.lastAgents ) { this.lastAgents = id; } // track last sent id
  }
  
}
