package go.takethespread.servlets;

import go.takethespread.fsa.FiniteStateAutomation;
import go.takethespread.managers.StatusListener;
import go.takethespread.managers.StatusManager;
import go.takethespread.util.LoginChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String log = req.getParameter("login");
        String pas = req.getParameter("password");
        boolean isUserLogin = new LoginChecker().verifyUser(log, pas);

        if (isUserLogin) {
//                startWork(); NOT HERE MAN!!!! I'M TELLING YOU
        }

        resp.getWriter().write(String.valueOf(isUserLogin));
        resp.getWriter().flush();

    }

    private void startWork() {
        new FiniteStateAutomation().start();
    }
}
