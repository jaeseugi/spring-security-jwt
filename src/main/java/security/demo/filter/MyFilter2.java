package security.demo.filter;

import javax.servlet.*;
import java.io.IOException;

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
public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("########### 필터 2 ###########");
        chain.doFilter(request, response);
    }
}
