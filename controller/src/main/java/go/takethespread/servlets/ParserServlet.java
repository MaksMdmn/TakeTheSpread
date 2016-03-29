package go.takethespread.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String consoleCommand = "";

        if(br != null){
            consoleCommand = br.readLine();
        }

        System.out.println(consoleCommand);
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
