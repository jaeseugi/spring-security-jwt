package security.demo.config.jwt;

/**
 * packageName :  security.demo.config.jwt
 * fileName : JwtProperties
 * author :  wotjr210
 * date : 2022/10/10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/10                wotjr210             최초 생성
 */
public interface JwtProperties {
    String SECRET = "cos";
    int EXPIRATION_TIME = 6000 * 10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
