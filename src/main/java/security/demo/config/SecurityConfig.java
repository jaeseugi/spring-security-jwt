package security.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import security.demo.filter.MyFilter1;
import security.demo.filter.MyFilter3;

/**
 * packageName :  security.demo.config
 * fileName : SecurityConfig
 * author :  wotjr210
 * date : 2022/10/09
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/09                wotjr210             최초 생성
 */

@Configuration
@EnableWebSecurity // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig {


    @Autowired
    private CorsFilter corsFilter;

    /*
        Spring Security without the WebSecurityConfigurerAdapter
        https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
     */
    @Bean // 반드시 스프링 빈으로 등록하기 위해 선언
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        /*
        BeanCreationException: ...
        The Filter class security.demo.filter.MyFilter1 does not have a registered order and cannot be added without a specified order.
        Consider using addFilterBefore or addFilterAfter instead.
         */
        //http.addFilter(new MyFilter1());

        // http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
        //  http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);  // 일반 필터와 순서 확인을 위한..
         http.addFilterAfter(new MyFilter3(), BasicAuthenticationFilter.class);  // 일반 필터와 순서 확인을 위한..
        /*
        addFilterBefore(), addFilterAfter() 둘다 아래와 동일하게 찍힌다.
        즉, 시큐리티가 우선적이다.
        ########### 필터 3 ###########
        ########### 필터 1 ###########
        ########### 필터 2 ###########
         */


        /*
            Rest API에서 csrf().disable()하는 이유
            이 이유는 rest api를 이용한 서버라면, session 기반 인증과는 다르게 stateless하기 때문에 서버에 인증정보를 보관하지 않는다.
            rest api에서 client는 권한이 필요한 요청을 하기 위해서는 요청에 필요한 인증 정보를(OAuth2, jwt토큰 등)을 포함시켜야 한다.
            따라서 서버에 인증정보를 저장하지 않기 때문에 굳이 불필요한 csrf 코드들을 작성할 필요가 없다.

            JWT 서버 세팅할때, 세션 사용한하고 cors 정책 허용하고, 폼로그인 사용 안하도록 설정하면됨.
            sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(corsFilter)
            .formLogin().disable()
         */
        http.csrf().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세선을 사용 안함. 웹은 상태를 유지하기 위해 쿠키와 세션을 이용하는데 그 방식을 사용 하지 않겠다는 의미.
                .and()
                .addFilter(corsFilter) // @CrossOrigin 사용하면 인증 관련 요청 X(인증X), 인증 관련되어 시큐리티 사용시 필터에 등록 인증(O)
                .formLogin().disable() // 폼 로그인 기반 사용 안함.
                .httpBasic().disable() // 기본적인 HTTP 로그인 방식을 사용 안함. 즉, ID,PW를 이용한 방식
                .authorizeRequests()
                .antMatchers("/api/vi/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll(); // 그외의 다른 URI는 전부 허용

        return http.build();
    }

}
