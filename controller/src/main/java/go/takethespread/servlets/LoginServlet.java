package go.takethespread.servlets;

import go.takethespread.fsa.FiniteStateAutomation;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties prop = loadProp(req);
        String log = req.getParameter("login");
        String pas = req.getParameter("password");

        if(prop.containsKey(log)){
            if(prop.getProperty(log).equals(pas)){
                resp.getWriter().write("true");
//                startWork();
            }else{
                resp.getWriter().write("false");
            }
        }else{
            resp.getWriter().write("false");
        }
    }

    private Properties loadProp(HttpServletRequest req){
        Properties prop = new Properties();
        try (InputStream input =  getServletContext().getResourceAsStream("WEB-INF/lobstaUsers.properties")) {
            if (input == null) {
                throw new RuntimeException("Settings-example file was unable to find");
            }
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    private void startWork(){
        new FiniteStateAutomation().start();
    }
}
