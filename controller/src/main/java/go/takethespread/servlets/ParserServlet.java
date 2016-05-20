package go.takethespread.servlets;

import go.takethespread.fsa.FiniteStateAutomation;
import go.takethespread.managers.InfoManager;
import go.takethespread.exceptions.ConsoleException;
import go.takethespread.exceptions.TradeException;

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
        InfoManager manager = InfoManager.getInstance();
        FiniteStateAutomation fsa = new FiniteStateAutomation();
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
