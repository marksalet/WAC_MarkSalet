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
        String waarde1 = req.getParameter("eerste_waarde");
        String waarde2 = req.getParameter("twee_waarde");
        String manier = req.getParameter("submit");
        
        double eersteWaarde = new Double(waarde1);
        double tweedeWaarde = new Double(waarde1);
        
        double waarde = 0;
        System.out.println(manier);
        if (manier.equals('x')) {
        	waarde = eersteWaarde * tweedeWaarde;
        } else if (manier.equals('/')) {
        	waarde = eersteWaarde / tweedeWaarde;
        } else if (manier.equals('-')) {
        	waarde = eersteWaarde - tweedeWaarde;
        } else if (manier.equals('+')) {
        	waarde = eersteWaarde + tweedeWaarde;
        }
        	
        
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("  <title>Dynamic Example</title>");
        out.println("  <body>");
        out.println("    <h2>Dynamic webapplication example</h2>");
        out.println("    <h2>Antwoord: " + waarde + "</h2>");
        out.println("  </body>");
        out.println("</html>");
    }
}
