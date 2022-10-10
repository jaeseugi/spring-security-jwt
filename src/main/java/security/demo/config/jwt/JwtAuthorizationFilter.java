package security.demo.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import security.demo.config.auth.PrincipalDetails;
import security.demo.model.User;
import security.demo.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * packageName :  security.demo.config.jwt
 * fileName : JwtAuthorizationFilter
 * author :  wotjr210
 * date : 2022/10/10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/10                wotjr210             최초 생성
 */

// 시큐리티가 filter를 가지고 있는데 그 필터ㅏ 중에 BasicAuthenticationFilter가 있음
// 권한이나 인증이 필요한 특정 주소를 요청 했을대 위 필터를 무조건 타게 되어 있음
// 만약에 권한이나 인증이 필요한 주고가 아니라면 이 필터를 안탐.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 인증이나 권한이 필요한 주소요청이 있을때 해당 필터를 타게됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 요청이 됨");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader = " + jwtHeader);

        // header가 있는지 체크
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        System.out.println("### jwtToken = " + jwtToken);
        String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        // 서명이 정상적으로 됨
        if (username != null) {
            User findUser = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(findUser);
            // 위의 로직대로 흘러오면 정상적안 사용자 이므로 강제로 Authentication을 생성한다.
            // JWT 토큰 서명을 통해서 정상이면 Authentication을 객체를 만들어준다.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
