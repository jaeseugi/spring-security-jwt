package security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.demo.model.User;

/**
 * packageName :  security.demo.repository
 * fileName : UserRepository
 * author :  wotjr210
 * date : 2022/10/10
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/10                wotjr210             최초 생성
 */
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
