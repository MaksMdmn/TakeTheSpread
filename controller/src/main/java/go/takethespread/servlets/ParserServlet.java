package go.takethespread.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import go.takethespread.managers.exceptions.ConsoleException;
import go.takethespread.managers.exceptions.TradeException;
import go.takethespread.managers.impl.ConsoleManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. get received JSON data from request
        String msg = request.getParameter("msg");
        String ans;

        System.out.println(msg);
        ConsoleManager manager = ConsoleManager.getInstance();
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
//
//        // 2. initiate jackson mapper
//        ObjectMapper mapper = new ObjectMapper();
//
//        // 3. Convert received JSON to Article
//        Article article = mapper.readValue(json, Article.class);
//
//        // 4. Set response type to JSON
//        response.setContentType("application/json");
//
//        // 5. Add article to List<Article>
//        articles.add(article);
//
//        // 6. Send List<Article> as JSON to client
//        mapper.writeValue(response.getOutputStream(), articles);
    }
}
