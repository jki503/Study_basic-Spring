package hello.core.member;

public class MemberServiceImpl implements MemberService{
//  private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository; // 인터페이스만 존재하여 추상화에만 의존. DIP를 지키는 것.

    public MemberServiceImpl(MemberRepository memberRepository) {
        //by using Constructor, Select the class for MemberServiceImpl
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}