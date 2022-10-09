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
public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 다운 캐스팅
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        System.out.println("########### 필터 3 ###########");

        rep.setCharacterEncoding("UTF-8");

        // 토큰 : cos 이걸 만들어줘야 함.
        // id, pw 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어주고 그걸 응답 해준다.
        // 요청 시 마다, 헤더에 Authrorization에 value값으로 토큰을 가지고 온다.
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지 검증만 하면된다. (RSA, HS256)
        if (req.getMethod().equals("POST")) {
            String headAuth = req.getHeader("Authorization");
            System.out.println("Authorization = " + headAuth);

            if (headAuth.equals("cos")) {
                chain.doFilter(request, response);
            } else {
                PrintWriter out = rep.getWriter();
                out.println("인증 안됨");
            }
        }
    }
}
