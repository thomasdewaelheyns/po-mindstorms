package penoplatinum.ui.server;

public class JSWrapper {

  public static String wrap(String method, String msg) {
    return "Dashboard." + method + "(" + msg + ");";
  }

  public static String start() {
    return wrap( "start", "" );
  }

  public static String error(String msg) {
    return wrap( "error", msg );
  }

  public static String updateModel(String ts, String robot, 
                                   int lightValue, String lightColor, int avgLightValue,
                                   int barcode, int sonarAngle, int sonarDistance,
                                   int ir1, int ir2, int ir3, int ir4, int ir5, int ir_dist,
                                   int walls, int value_n, int value_e, int value_s, int value_w,
                                   String event, String source, String plan, String queue, String action,
                                   String argument, int rate )
  {                                   
    return wrap( "update","'" + ts + "','" + robot + "'," +
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

  public static String updateWalls(String grid, String ts, String robot, 
                                    String updates)
  {
    return wrap( grid + ".updateWalls", "'" + ts + "','" + robot + "'," +
                      "{" + updates + "}" );
  }

  public static String updateValues(String grid, String ts, String robot, 
                                    String updates)
  {
    return wrap( grid + ".updateValues", "'" + ts + "','" + robot + "'," +
                      "{" + updates + "}" );
  }
  
  public static String updateAgents(String grid, String ts, String robot, 
                                    String updates)
  {
    return wrap( grid + ".updateAgents", "'" + ts + "','" + robot + "'," +
                      "{" + updates + "}" );
  }
}