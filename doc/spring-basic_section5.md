---
title: "[section5] 스프링 컨테이너와 스프링 빈"
category: basic-Spring
tags: [Spring Container, Spring Bean]
Author: Jung
---

## 스프링 컨테이너와 스프링 빈

### 스프링 컨테이너

- `BeanFactory`, `ApplicationContext`로 `스프링 컨테이너` 사용.

  > 주로 BeanFactory를 상속받은 `ApplicationContext`를  
  > `스프링 빈`을 사용한다.

- 기존 `JAVA` AppConfig 설정 정보 구성 -> `@Bean`을 이용하여 스프링 컨테이너 등록
- 컨테이너에서 `applicationContext.getBean()` 메서드로 스프링 빈 조회 후 객체 사용
- [스프링 컨테이너가 주는 장점 중 하나는 싱글톤 패턴](./spring-basic_section6.md)

### 스프링 빈

1. 등록

```java
@Bean //Resister to the Spring container
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());

    }
```

|   Bean Name   |    Bean Object    |
| :-----------: | :---------------: |
| memberService | MemberService@x01 |

> `@Bean 어노테이션` 이용하여 `스프링 컨테이너` 등록

2. 조회

- 컨테이너에 등록된 모든 빈 조회

```java
@Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() { //Spring 내부 빈까지 조회
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = " + beanDefinitionName + " object" + bean);
        }

    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() { // 내가 등록한 빈 조회
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            //Role ROLE_APPLICATION : 직접 등록한 애플리케이션 빈
            //Role ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
          if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object" + bean);
            }
        }

    }
```

> 실제 테스트에서는 console에 찍지 않음.  
> 내가 등록한 빈을 조회할때는 BeanDefinition Role을 `ROLE_APPLICAITON`으로 조회

- 스프링 빈 조회 - 기본

```java
@Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){ //인터페이스로 조회
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType(){
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2(){ //반환 타입으로 구체로 지정해도 되지만, 구현체에 의존한 테스트
        MemberServiceImpl memberServiceImpl = ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberServiceImpl).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("Failed: 빈 이름으로 조회X")
    void findBeanByNameX(){
//      MemberService xxxx = ac.getBean("xxxx", MemberService.class);
        assertThrows(NoSuchBeanDefinitionException.class,()->ac.getBean("xxxx", MemberService.class));
    }
```

> 실제 구체 타입으로 빈을 조회할 일도 없고, 좋지 않은 방식.

- 스프링 빈 조회 - 동일한 타입이 둘 이상

```java
@Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면, 중복 오류 발생")
    void findBeanByTypeDuplicate(){
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(MemberRepository.class));
    }

    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면, 빈 이름을 지정하면 된다")
    void findBeanByName(){
        MemberRepository memberRepository = ac.getBean("memberRepository1",MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }


    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    void findAllBeanByType(){
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
        System.out.println("beansOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Configuration
    static class SameBeanConfig {

        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }
        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }
```

> bean type의 amiguoty로 인한 오류 발생 가능성.  
> bean 이름으로 조회 하는 것이 대부분 적합한 방식.

- 스프링 빈 조회 - 상속 관계

```java
@Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 있으면, 중복 오류가 발생한다")
    void findBeanByParentTypeDuplicate(){
        DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모 타입으로 조회시, 자식 둘 이상 있으면, 빈 이름을 지정하면 된다.")
    void findBeanByParentTypeBeanName(){
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("특정 하위 타입으로 조회") // 안 좋은 방법
    void findBeanBySubType(){
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllParentType(){
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value =" + beansOfType.get(key));
        }
    }

    @Configuration
    static class TestConfig {
        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
```

> 부모타입 역시 중복 되면 에러 발생.  
> 하위타입으로 조회하는 것은 부적합 -> 이것 역시 `구현체에 의존 하는 것과 같음`.

3. `BeanFactory` 대신 `ApplicationContext`를 사용하는 이유

- BeanFactory

  > 최상위 인터페이스  
  > 스프링 빈을 관리 및 조회하는 역할  
  > getBean()을 제공하는 인터페이스

- ApplicationContext

  > BeanFactory를 상속 받아 기능 제공  
  > `애플리케이션 개발시 부가 기능`을 제공

- ApplicationContext가 제공하는 부가 기능

> `메시지 소스`로 국제화 기능  
> `환경 변수` : 로컬, 개발, 운영 구분  
> `애플리케이션 이벤트`: 이벤트 발행 및 구독 모델 지원  
> `리소스 조회` : 파일 클래스, 외부 등에서 리소스 조회 편의성
