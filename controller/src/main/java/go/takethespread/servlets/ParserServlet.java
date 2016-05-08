package go.takethespread.servlets;

import go.takethespread.fsa.FinitStateAutomation;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.exceptions.ConsoleException;
import go.takethespread.managers.exceptions.TradeException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ParserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = request.getParameter("msg");
        String ans;

        System.out.println(msg);
        ConsoleManager manager = ConsoleManager.getInstance();
        FinitStateAutomation fsa = new FinitStateAutomation();
        fsa.start();
        try {
            ans = manager.parseConsoleMsg(msg);
        } catch (ConsoleException e) {
            ans = e.toString();
            e.printStackTrace();
        } catch (TradeException e) {
            ans = e.toString();
            e.printStackTrace();
        }

        response.getWriter().write(ans);
        response.getWriter().flush();
    }
}
