---
title: "[section7] 의존 관계 주입 방법"
category: basic-Spring
tags: [스프링, 의존 관계 주입, 생성자 주입]
Author: Jung
---

## `의존관계 주입 방법`

---

</br>
</br>

1. 생성자 주입

- 생성자를 통해서 의존 관계 주입 받는 방법
- 단 1번, 생성자 호출 시점에서 호출되는 것을 보장.
- `불변`, `필수` 의존관계 사용

> `권장사항`
>
> - 애플리케이션 종료시점까지 의존 관계 변경할 일이 없음.
> - 대부분의 의존 관계는 종료전까지 불변해야한다.
> - 변경을 사전에 lock
> - 누락 예방
>   - 수정자에서 의존관계 주입을 빠뜨릴 경우 NPE 발생.
>   - 생성자 주입 사용시 `컴파일 오류`로 누락된 의존관계 찾기 용이

2. 수정자(setter) 주입

- settter로 필드 값 변경하는 수정자 메서드를 통해 의존관계 주입.
- 선택, 변경 가능성 있는 경우 사용

3. 필드 주입

- 필드 값에 바로 주입하는 방법
- 외부에서 변경 불가함으로 테스트 어려움
- DI 프레임워크 없으면 불가능
- `지양 사항`
- `@Configuration`이 있는 스프링 설정을 목적으로 하는 용도로만 사용

4. 일반 메서드 주입

- 일반 메서드로 의존관계 주입하는 방법
- 한 번에 여러 필드 주입 받을 수 있음.

</br>
</br>

## 롬복 사용으로 생성자 주입

---

### 롬복 라이브러리 적용 방법

- build.gradle

```java
configurations {
    compileOnly {
        extendsFrom annotationProcessor
}

dependencies{

  ...
  //lombok 라이브러리 추가 시작
  compileOnly 'org.projectlombok:lombok'
  annotationProcessor 'org.projectlombok:lombok'
  testCompileOnly 'org.projectlombok:lombok'
  testAnnotationProcessor 'org.projectlombok:lombok'
  //lombok 라이브러리 추가 끝

}
```

- Preferences -> plugin -> lombok 설치 및 실행 후 재시작
- Preferences -> Annotation Processors 검색 -> Enable annotation processing 체크 (재시작)
- 임의의 테스트 클래스 만든 후 @Getter, @Setter 확인

</br>
</br>

### @RequiredArgsConsturctor

> 생성자에서 필드에 의존 관계를 주입하는 코드 생략 가능하다!

- 기존 생성자 주입 방식

```java

@Component
public class Name{

  private final 필드;
  ...

  @Autowired
  public 생성자(필드...){

  }
}

```

```java

@Component
@RequiredArgsConstructor
public class Name{
  private final 필드;


}

```

</br>
</br>

## @Autowired type으로 bean 조회

---

> @Autowired로 주입한 의존관계의 하위 타입을 스프링 빈으로 선언하면 -> NoUniqueBeanDefinitionException 오류
> 하위 타입 조회 가능 -> but, DIP 위반 및 유연성 저하

</br>
</br>

### 문제 해결

1. @Autowired 필드 명 매칭

- 상위 타입의 필드명을 하위타입 빈의 이름으로 변경
- 즉, 필드명 매칭은 타입 매칭 후, 같은 타입이 있을 경우 추가적으로 이름 조회

2. @Qualifier

- 추가 구분자 붙여 주는 방법 (빈 이름 변경X)
- 빈 선언 후(@Component) @Qualifier("naming") 붙여주기.
- @Qualifer에서 못찾으면 빈에서 찾음. (but, 구분 해서 사용하기.)
- @Qualifer끼리 매칭
- 빈 이름 매칭 (사용 X)
- 의존 관계 주입시 @Qualifer를 붙여서 파라미터를 넣어야한다는 단점.

3. @Primary

- @Autowired시 여러 빈이 매칭될 경우 우선순위 결정.

> @Primary, @Qualifier
>
> - @Qualifier가 더 상세하게 동작하여 우선권이 높음.

</br>
</br>

## 자동, 수동 올바른 실무 운영 기준

---

- 자동기능을 기본으로.

- 수동 등록 빈은 언제?
  - 업무 로직 빈
    - 컨트롤러
    - 핵심 비즈니스 로직이 있는 서비스
    - 데이터 계층의 로직 처리하는 리포지토리
  - 기술 지원 빈
    - 기술적인 문제
    - AOP 처리

> 업무 로직 -> 자동 빈 등록
>
> - 문제가 발생해도 파악하기 쉽고,
> - 일정한 패턴이 있음.
>
> 기술 지원 -> 수동 빈 등록
>
> - 숫자가 적고, 애플리케이션 전반에 걸쳐 영향 미침.
> - 파악 하기 어려움으로 수동 빈 등록으로 명확성
