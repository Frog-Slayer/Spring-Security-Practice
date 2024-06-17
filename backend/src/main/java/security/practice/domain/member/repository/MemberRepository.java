package security.practice.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.practice.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);

}
