package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.tree.ExpandVetoException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Commit
    public void 회원가입() throws Exception {
        //given
        Member member = new Member("spring", new Address("city", "street", "zipcode"));

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberService.findMember(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복체크() throws Exception{
        //given
        Member member = new Member("spring", new Address("city", "street", "zipcode"));
        Member member1 = new Member("spring", new Address("city", "street", "zipcode"));


        //when
        memberService.join(member);
        memberService.join(member1);
        //then
        fail("예외가 발생해야 합니다.");
    }

    @Test
    public void 회원전체조회() {
    }
}