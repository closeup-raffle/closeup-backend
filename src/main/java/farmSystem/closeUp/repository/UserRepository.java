package farmSystem.closeUp.repository;

import farmSystem.closeUp.domain.SocialType;
import farmSystem.closeUp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickName(String nickName);

    /***
     소셜 타입과 소셜의 식별값으로 회원 찾는 메소드.
     추가 정보를 입력받아 회원 가입을 진행할 때 소셜 타입, 식별자로 해당 회원을 찾기 위한 메소드
     ***/
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
