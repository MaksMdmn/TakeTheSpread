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

public class SettingsUpdateServlet extends HttpServlet {
    private StatusListener statusListener = StatusManager.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Settings key = Settings.valueOf(req.getParameter("key"));
        String value = req.getParameter("value");
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        String answer = info.updateLocalValue(key, value);

        System.out.println("java answer: " + answer);
        resp.getWriter().write(answer);
        resp.getWriter().flush();

        statusListener.settingsStatucChanged();
        System.out.println(info.getSettingsMap().toString());
        System.out.println(info.commis);
    }
}
