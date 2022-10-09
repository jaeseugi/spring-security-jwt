package security.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * packageName :  security.demo.model
 * fileName : User
 * author :  wotjr210
 * date : 2022/10/09
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/10/09                wotjr210             최초 생성
 */

@Data
@Entity
@Table(name = "USERS")
public class User {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String roles; // USER, ADMIN

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
