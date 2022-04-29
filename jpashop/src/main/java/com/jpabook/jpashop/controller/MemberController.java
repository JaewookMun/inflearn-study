package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        // model.addAttribute를 사용할 때는 넣는 내용을 명시적으로 표시하는 것을 선호함.
        // 엔티티를 그대로 전하기 보다는 화면에 맞는 DTO 객체를 전달하는 것이 좋다.
        return "members/memberList";
    }

    // jpa를 사용할 때 엔티티는 의존성이 없어야함 최대한 순수하게 유지해야함.
    // 화면 관련된 기능을 넣으면 안됨. 이것은 DTO, Form 객체를 만들어 활용

    // api를 만들 때는 엔티티를 제공하면 안된다. - api는 SPEC을 의미한다.
    // 엔티티는 외부로 노출하면안된다.
}
