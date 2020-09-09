package jpabook.jpashop.api;

import jdk.jfr.DataAmount;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    //Entity를 외부로 노출시키면 안됨
    // 1. api스펙문제 - entity의 필드를 변경시 api 스펙이 바뀜
    // 2. entity의 모든 값이 노출됨
    // 3. 하나의 entity에 용도가 다른 여러 api가 만들어질 경우, entity에 한 api 스펙을 맞추기위한 로직(ex-JsomIgnore)이 추가되면
    // 다른 api 스펙을 맞추기 어렵다
    // 4. 컬렉션을 직접 반환하면 추가적인 api 스펙 변경이 어렵다.
    // ex)반환한 엔티티 배열의 size를 추가할 경우 List를 반환하는게 아닌 별도의 Result클래스 객체를 반환해야 한다.
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    //entity를 직접 반환하는 대신 DTO를 반환
    @GetMapping("api/v2/members")
    public Result membersV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;

    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
    
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member(request.getName(), new Address("","",""));
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());

        Member member = memberService.findMember(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }



    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

}
