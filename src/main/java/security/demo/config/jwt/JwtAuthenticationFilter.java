package security.demo.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.demo.config.auth.PrincipalDetails;
import security.demo.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * packageName :  security.demo.config.jwt
 * fileName : JwtAuthenticationFilter
 * author :  wotjr210
 * date : 2022/10/10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/10                wotjr210             최초 생성
 */

/*
    스프링 시큐리티에서 UsernamePasswordAuthenticationFilte가 있음.
    /login 요청해서 username, password 전송하면(POST)
    UsernamePasswordAuthenticationFilter가 동작을 해야됨.

    하지만, formLogin().disable()했기 때문에 작동하지 않음.
    그래서 다시 작동하게 하기 SecurityConfig에 등록을 해줘여함.
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // 이놈을 통해서 로그인 시도하면됨.
    private final AuthenticationManager authenticationManager;


    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수 입니다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter.attemptAuthentication 로그인 시도 중");

        // 1. username, password 받아서
        try {

//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while ((input = br.readLine()) != null) {
//                System.out.println("input = " + input);
//            }
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user = " + user);

            // formLogin이면 자동으로 생성 해주는데 우리는 직접 해야됨.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 이때, PrincipalDetailsService의 loadUserByUsername() 함수가 실행된다.
            // 실행 후 정상이면 authentication이 리턴됨.
            // DB에 있는 username과 password가 일치한다.
            // authentication에 내가 로그인한 정보가 담긴다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 인증 완료후 Authentication객체는 session영역에 저장된다. => 로그인이 되었다는 뜻
            PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
            System.out.println("로그인 완료됨! = " + principalDetails.getUser().getUsername());

            // Authentication객체가 세션영역에 저장을 해야하는데 그 방법이 리턴입니다.
            // 리턴의 이유는 권환 관리를 시큐리티가 대신해주기 때문에 편하려고 하는거임
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 필요 없음, ㅅ근데 단지 권한 처리 떄문에 세션 넣어줌.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("===================");

        // 2. 정상인지 로그인 시도. authenticationManager로 로그인 시도를 하면!! PrincipalDetailsService가 호출되면서 loadUserByUsername이 호출

        // 3. PrincipalDetails를 세션에 담고 (권환 관리를 위해 세션에 담음)

        // 4. JWT 토큰을 만들어서 응답해주면 됨.

        return null;
    }

    // attemptAuthentication실행후 인증이 정상적으로 실해되면 successfulAuthentication 함수가 실행됨.
    // JWT 토큰 만들어서 요청한 사용자헤게 JWT토큰을 응답해주면됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻!");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA 방식(공개키,개인키)은 아니고 Hash암호 방식
        // HMAC512의 특징은 시크릿키를 가지고 해쉬 암호화를 한다.
        String jwtToken = JWT.create()
                .withSubject("cos 토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (6000 * 10))) // 10분
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
