package go.takethespread.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatusCheckServlet extends HttpServlet {
    private static final String RUN_PROGRAM_CHECK = "isRun";
    private static final String NEW_ORDERS_CHECK = "isOrd";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String checkParam = req.getParameter("checkParam");
        switch (checkParam) {
            case RUN_PROGRAM_CHECK:
                break;
            case NEW_ORDERS_CHECK:
                break;
            default:
                break;
        }
    }
}
