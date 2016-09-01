package go.takethespread.servlets;

import go.takethespread.Settings;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.StatusListener;
import go.takethespread.managers.StatusManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateSettingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Settings key = Settings.valueOf(req.getParameter("key"));
        String value = req.getParameter("value");
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        String answer = info.updateLocalValue(key, value);

        resp.getWriter().write(answer);
        resp.getWriter().flush();
    }
}
