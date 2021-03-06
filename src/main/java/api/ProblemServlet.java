package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import problem.Problem;
import problem.ProblemDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProblemServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");
        if (id == null || "".equals(id)) {
            // 没有 id 这个参数, 执行 "查找全部" 逻辑
            selectAll(resp);
        } else {
            // 存在 id 这个参数, 执行 "查找指定题目" 逻辑
            selectOne(Integer.parseInt(id), resp);
        }
    }

    private void selectAll(HttpServletResponse resp) throws IOException {
        // Content-Type 描述了 body 中的数据的类型是啥样的.
        // 常见的取值:
        // html:  text/html
        // 图片:  image/png   image/jpg
        // json:  application/json
        // css:   text/css
        // javascript: application/javascript
        // 此处还能同时设定字符集
        resp.setContentType("application/json; charset=utf-8");
        ProblemDAO problemDAO = new ProblemDAO();
        List<Problem> problems = problemDAO.selectAll();
        // 把结果组织成 json 结构.
        // [注意!] 需要把 problem 中的有些字段去掉.
        String jsonString = gson.toJson(problems);
        resp.getWriter().write(jsonString);
    }

    private void selectOne(int problemId, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        ProblemDAO problemDAO = new ProblemDAO();
        Problem problem = problemDAO.selectOne(problemId);
        // 测试代码不应该告诉前端用户. 此时手动把这个内容清理掉.
        problem.setTestCode("");
        String jsonString = gson.toJson(problem);
        resp.getWriter().write(jsonString);
    }
}
