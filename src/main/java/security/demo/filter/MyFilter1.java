package security.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * packageName :  security.demo.filter
 * fileName : MyFilter1
 * author :  wotjr210
 * date : 2022/10/09
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/09                wotjr210             최초 생성
 */
public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("########### 필터 1 ###########");
        chain.doFilter(request, response);
    }
}
