package org.example.demo1;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logData(request);
        //get the forwarded value via post method
        String page = request.getParameter("page");
        String destination = "";
        if (page.equals("page1")) {
            destination = "/page1.jsp";
        } else if  (page.equals("page2")) {
            destination = "/page2.jsp";
        } else {
            destination = "/index.jsp";
        }

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        RequestDispatcher rd = request.getRequestDispatcher(destination);
        rd.forward(request, response);

    }

    public void logData(HttpServletRequest request) throws ServletException, IOException {
        StringBuilder log_msg = new StringBuilder();
        String http_method =  request.getMethod();
        String ip_address = request.getRemoteAddr();
        String user_agent = request.getHeader("User-Agent");
        String client_language = request.getHeader("Client-Language");
        String parameter = request.getParameter("page");

        log_msg.append(http_method).append("\n");
        log_msg.append(ip_address).append("\n");
        log_msg.append(user_agent).append("\n");
        log_msg.append(client_language).append("\n");
        log_msg.append(parameter).append("\n");

        System.out.println(log_msg.toString());
    }

    public void destroy() {
    }
}