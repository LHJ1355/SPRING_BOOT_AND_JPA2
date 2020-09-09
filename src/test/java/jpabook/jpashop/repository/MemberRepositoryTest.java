package jpabook.jpashop.repository;

import javassist.bytecode.annotation.MemberValue;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @PersistenceContext
    MemberRepository memberRepository;

    @Test
    public void save() {
        //given
        Member member = new Member("spring", new Address("city", "street", "zipcode"));

        //when
        memberRepository.save(member);
        Long saveId = member.getId();
        Member saveMember = memberRepository.findOne(saveId);

        //then
        Assertions.assertThat(member).isEqualTo(saveMember);
    }

    @Test
    public void find() {
    }
}