package go.takethespread.servlets;

import go.takethespread.exceptions.ConsoleException;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.ConsoleManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConsoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConsoleManager manager = ConsoleManager.getInstance();
        String userMessage = req.getParameter("userMessage");
        String answer;

        System.out.println(userMessage);

        try {
            answer = manager.parseConsoleMsg(userMessage);
            resp.getWriter().write(answer);
            resp.getWriter().flush();
        } catch (ConsoleException e) {
            e.printStackTrace();
        } catch (TradeException e) {
            e.printStackTrace();
        }

    }
}
