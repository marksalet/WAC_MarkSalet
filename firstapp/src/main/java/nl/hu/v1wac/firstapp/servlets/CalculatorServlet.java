package nl.hu.v1wac.firstapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/CalculatorServlet.do")
public class CalculatorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)   
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        String nr1 = req.getParameter("nr1");
        String nr2 = req.getParameter("nr2"); 
        
        int nr1Con = Integer.parseInt(nr1);
        int nr2Con = Integer.parseInt(nr2);
        int answer = 0;
        String operator = null;
        
		if("+".equals(action)) {
        	answer = nr1Con + nr2Con;
        	operator = "+";
        }
        
        if("-".equals(action)) {
        	answer = nr1Con - nr2Con;
        	operator = "-";
        }
        
        if("x".equals(action)) {
        	answer = nr1Con * nr2Con;
        	operator = "x";
        }
        
        if(":".equals(action)) {
        	answer = nr1Con / nr2Con;
        	operator = ":";
        }
        
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("  <title>Dynamic Example</title>");
        out.println("  <body>");
        out.println("    <h2>Dynamic webapplication example</h2>");
        out.println("    <h2> The calculator calculated the following answer: " + answer);
        
        out.println("  </body>");
        out.println("</html>"); 
        }
}


