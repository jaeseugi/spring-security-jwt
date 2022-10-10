package security.demo.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.demo.model.User;
import security.demo.repository.UserRepository;

/**
 * packageName :  security.demo.config.auth
 * fileName : PrincipalDetailsService
 * author :  wotjr210
 * date : 2022/10/10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/10                wotjr210             최초 생성
 */

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
        http://localhost:8080/login > .formLogin().disable()에 의해 동작안함.
        그래서 PrincipalDetailsService로 직접 요청을 때려주는 필터 만들어야됨...
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService.loadUserByUsername");
        User findUser = userRepository.findByUsername(username);
        return new PrincipalDetails(findUser);
    }
}
