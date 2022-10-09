package security.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import security.demo.filter.MyFilter1;
import security.demo.filter.MyFilter2;

/**
 * packageName :  security.demo.config
 * fileName : FilterConfig
 * author :  wotjr210
 * date : 2022/10/09
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/09                wotjr210             최초 생성
 */


@Configuration // IoC에 의해 메모리에 로딩된다.
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        /*
            파라미터로 필터 객체 안넣으면 예외 발생
            Filter must not be null
         */
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 낮은 번ㄴ호가 필터 중에 가장 먼저 실행된다.
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
