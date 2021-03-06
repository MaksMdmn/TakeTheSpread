package go.takethespread.servlets;

import go.takethespread.util.ParseJsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndicatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonObj;
        if (ParseJsonUtil.checkConn() && ParseJsonUtil.checkDataChannel()) {
            jsonObj = ParseJsonUtil.indicatorsToJson();
        } else {
            jsonObj = null; //HERE????
        }

        resp.setContentType("application/json");
        resp.getWriter().print(jsonObj);
        resp.getWriter().flush();
    }
}