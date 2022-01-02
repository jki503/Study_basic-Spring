package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* Before Spring
public class AppConfig {
    public MemberService memberService(){
        // before refactoring
        // return new MemberServiceImpl(new MemoryMemberRepository()); // 이때 class가 들어, 생성자 주입.
        return new MemberServiceImpl(memberRepository()); // 이때 class가 들어, 생성자 주입.

    }

    public MemberRepository memberRepository() { // 예를 들어 DB 정책이 바뀔 경우 이 코드만 수정하면 된다.
        return new MemoryMemberRepository();
    }

    public OrderService orderService(){
        //before refactoring
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy(){
//      return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}*/

//migration to Spring
@Configuration
public class AppConfig {

    @Bean //Resister to the Spring container
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());

    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService(){
        //before refactoring
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy(){
//      return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
