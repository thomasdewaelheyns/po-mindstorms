package penoplatinum.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RobotsServlet extends HttpServlet {

  public void service( HttpServletRequest request,
                       HttpServletResponse response ) 
                       throws ServletException, IOException 
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    DataProvider dp = null;
    try {
      dp = new DataProvider().connect();
      ResultSet rs = dp.getRS( "SELECT DISTINCT robot FROM model;" );
      while(rs.next()) {
        out.println(rs.getString(1));
      }
    } catch( Exception e ) {
      // :-(
      throw new RuntimeException(e);
    } finally {
      out.close();
      if(dp != null) { dp.disconnect(); }
    }
  }
}
