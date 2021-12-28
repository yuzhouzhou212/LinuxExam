
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/CreateBook")
public class CreateBook extends HttpServlet {
   final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   final static String URL = "jdbc:mysql://116.62.198.183/linux_final";
   final static String USER = "root";
   final static String PASS = "LLliulu212/";
   final static String SQL_INSERT_NOTEPAD = "INSERT INTO t_book(id, name,author) VALUES (?, ?,?)";

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      Book req = getRequestBody(request);
      getServletContext().log(req.toString());
      PrintWriter out = response.getWriter();

      out.println(createBook(req));
      out.flush();
      out.close();
   }

   private Book getRequestBody(HttpServletRequest request) throws IOException {
      Book book = new Book();
      StringBuffer bodyJ = new StringBuffer();
      String line = null;
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null)
         bodyJ.append(line);
      Gson gson = new Gson();
      book = gson.fromJson(bodyJ.toString(), new TypeToken<Book>() {
      }.getType());
      return book;
   }

   private int createBook(Book req) {
      Connection conn = null;
      PreparedStatement stmt = null;
      int retcode = -1;
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(URL, USER, PASS);
         stmt = conn.prepareStatement(SQL_INSERT_NOTEPAD);

         stmt.setInt(1, req.id);
         stmt.setString(2, req.name);
         stmt.setString(3, req.author);
         int row = stmt.executeUpdate();
         if (row > 0)
            retcode = 1;

         stmt.close();
         conn.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
      return retcode;
   }
public class Book {
    int id;
    String name;
    String author;

    @Override
    public String toString() {
        return "Book [author=" + author + ", id=" + id + ", name=" + name + "]";
    }

}}
