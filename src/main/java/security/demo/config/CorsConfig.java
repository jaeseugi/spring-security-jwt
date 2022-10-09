package security.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * packageName :  security.demo.config
 * fileName : CorsConfig
 * author :  wotjr210
 * date : 2022/10/09
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/09                wotjr210             최초 생성
 */

//@CrossOrigin // 인증 필요하지 않는 요청만 허용시킨다. 즉, 시큐리티를 통한 인증 요청을 사용할 수 없게 된다.
@Configuration
public class CorsConfig {

    /**
     * 스프링 시큐리티를 이용한 CORS 설정
     * 생성 후 필터에 걸어야한다.
     * 빈 등록 할때는 public
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답할 때  json을 자바스크립트에서 처리할 수 있게 할지를 설정한다.
        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용 하겠다.
        config.addAllowedHeader("*"); // 모든 HTTP 헤더에 응답을 허용 하겠다.
        config.addAllowedMethod("* ");//  모든 HTTP 메소드에 응답을 허용 하겠다.
        source.registerCorsConfiguration("/api/**", config); // /api/** 로 들어오는 모든 요청은 config의 설정을 따른다.
        return new CorsFilter(source);
    }
}
