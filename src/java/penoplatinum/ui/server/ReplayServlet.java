package penoplatinum.ui.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReplayServlet extends HttpServlet {

  public void service( HttpServletRequest request,
                       HttpServletResponse response ) 
                       throws ServletException, IOException 
  {
    new Streamer().rewind().service(request, response);
  }
}
