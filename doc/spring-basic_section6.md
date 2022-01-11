---
title: "[section6] 컴포넌트 스캔과 자동 주입하기"
category: basic-Spring
tags: [스프링, 컴포넌트 스캔, 의존 관계]
Author: Jung
---

## **컴포넌트 스캔과 의존 관계 자동 주입하기**

---

</br>
</br>

### **`Why?`**

---

> - 일일이 개발자가 `AppCofig`를 통해 `@Bean`을 설정하는 것은 번거로움.
> - 또한 누락할 확률 높아짐.

</br>
</br>

### **`@Component` 와 `@Autowired`로 Spring container 등록 & 의존관계 주입.**

---

</br>

```java
@Component
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository; // 인터페이스만 존재하여 추상화에만 의존. DIP를 지키는 것.

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        //by using Constructor, Select the class for MemberServiceImpl
        this.memberRepository = memberRepository;
    }
}
```

</br>

```java
@Component
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired // 컨텍스트에서 두개의 파라미터를 자동으로 주입해준다.
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

</br>

```java
@Component
public class MemoryMemberRepository implements  MemberRepository{

}
```

</br>

```java
@Component
public class RateDiscountPolicy implements DiscountPolicy{

}
```

</br>

> - 스프링 컨테이너에 등록할(`이전 수동 등록 @Bean`) class level에 `@Component`
> - 생성자에 @Autowired로 의존 관계 주입
>   - 스프링 컨테이너가 생성자 파라미터에 해당하는 스프링 빈을 찾아서 주입

</br>
</br>

### **@`ComponentScan`으로 `@Component` 스캔 대상**

---

</br>

```java

@`ComponentScan`(
  basePackages = "package1.pakage2"
  basePackageClasses= "className.class"
)

```

> - `basePackages` : 지정한 패키지를 스캔 시작 위치 지정
>   - `default `: `ComponentScan`이 붙은 설정 정보 클래스의 패키지가 시작 위치.
>   - Springboot에서 `ComponentScan`을 붙이지 않아도 동작함
>     - `@SpringBootApplication`에 `ComponentScan`이 붙어 있음.
> - basePackageClasses : 지정한 클래스의 패키지를 스캔 시작 위치로 지정

</br>
</br>

#### **관례**

> - com.package1 -> 시작 루트에 설정 class 지정
> - 스프링 부트에서는 `@SpringBootApplication`를 프로젝트 시작 루트에 위치 시키는 것.(`ComponentScan` 들어있음)

</br>
</br>

### Filter를 이용한 ComponentScan 대상 지정

---

</br>
</br>

```java
@ComponentScan(
        includeFilters = @Filter(type = FilterType.ANNOTATION, classes =  MyIncludeComponent.class),
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =  MyExcludeComponent.class)
    )
```

</br>

> - `includeFilters` : MyIncludeComponent(custom 어노테이션임) 어노테이션이 붙은 class 포함
>
> - `excludeFilters` : MyExcludeComponent(custom 어노테이션임) 어노테이션이 붙은 class 제외

</br>
</br>

#### Filter Type 옵션

---

- ANNOTATION : 기본값, 어노테이션 인식
- ASSIGNABLE_TYPE : 지정한 타입과 자식 타입 인식
- ASPECT : ASPECTJ 패턴
- REGEX : 정규 표현식
- CUSTOM : TypeFilter 인터페이스 구현해서 처리
