package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    /**
     * api를 만들 때는 엔티티를 외부에 노출시키지 않고
     * dto 를 통해 api spec에 적합한 데이터를 전달한다.
     *
     */



    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        // 회원정보를 원하는 것인데 entity 를 뿌리면 entity에 있는 정보가 모두 노출됨.
        // @JsonIgnore, 별도의 로직으로 처리 할 수 있지만 변동성존재.
        // 클라이언트 별로 다양한 형태의 회원정보를 받을 수 있는데
        // 엔티티로만 이르 처리하면 어려움.
        // 엔티티 내부에 화면 출력을 위한 기능을 추가하면 좋지 않음.
        // 엔티티를 변경했을 때 api가 영향을 받으면 안됨.!
        return memberService.findMembers();
    }

    // 위처럼 리스트만 보내면 확장성에 제한이 있기 때문에 껍데기를 씌워 데이터를 전달.
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }
    // entity를 dto 로 바꾸어줌.

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    // @RequestBody - json body로 온 데이터를 Member로 전환
    // 외부에서 오는 데이터를 entity에 그대로 매핑시키면 안된다.
    // Entity를 외부에 노출시키면 좋지 않다. - 파라미터에 넣지 않는다.
    // 외부에서 오는 데이터는 별도의 DTO로 받는 것이 좋다.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // DTO를 외부에 사용해서 API SPEC에 맞추어준다. - Entity를 노출했을 때 발생할 수 있는 문제를 해결할 수 있는 방법
    // DTO를 통해 Entity가 바뀌더라도 api 스팩에 영향을 주지 않도록 처리한다.
    // api spec을 까보지 않으면 어떤 값이 넘어왔는지 알 수 없다.
    // api SPEC에 맞추어서 어떤 값이 들어오는지 확인할 수 있다. - api spec에 맞는 dto를 만들어 받는 것이 정석에 가깝다.
    // presentation layer 에서 넘어온 값에 대한 validation 은 @Valid 에서 처리
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        // update쿼리와 조회 커맨드는 분리하여 코드를 구성
        // -> 유지보수 측면에서 좋음.
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
