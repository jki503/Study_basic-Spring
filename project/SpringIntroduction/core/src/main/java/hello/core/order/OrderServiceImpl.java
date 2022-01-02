package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{
//  private final MemberRepository memberRepository = new MemoryMemberRepository();
//  private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//  private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); 정책 변경할때 구현체를 수저함으로 DIP 어기고, 클라이언트 수정으로 OCP 어김
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        discountPolicy.discount(member,itemPrice);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}