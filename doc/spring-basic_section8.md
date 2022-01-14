---
title: "[section8] 빈 생명주기 콜백"
category: basic-Spring
tags: [스프링, 생명주기 콜백, @PostConstruct, @PreDestroy]
Author: Jung
---

## 빈 생명주기 콜백

---

</br>
</br>

### 스프링 빈 라이프 사이클

</br>

> 객체 생성 -> 의존관계 주입
>
> - 즉 객체를 생성하고 의존관계를 주입후 데이터 사용 가능
> - 콜 백 메서드를 통해 초기화 시점 및 소멸 시점에서 이벤트 사용 가능

</br>
</br>

### 스프링 빈 이벤트 라이프 사이클

</br>

1. 스프링 컨테이너 생성
2. 스프링 빈 생성
3. 의존관계 주입
4. 초기화 콜백 : 빈 생성과 의존 관계 주입 후 호출
5. 사용
6. 소멸전 콜백 : 빈이 소멸 되기 직전에 호출
7. 스프링 종료

</br>

> 객체의 생성과 초기화를 분리하자.  
> -> 유지보수 관점에서 좋음
>
> - 생성자는 파라미터 받은 후, 메모리 할당 하여 객체 생성하는 책임
> - 초기화는 생성된 인스턴스 활용하여 외부 커넥션을 연결하는 무거운 동작 수행

</br>
</br>

### 콜백 방식

- 인터페이스

```java
public class NetworkClient implements InitializingBean, DisposableBean{

 @Override
 // InitializingBean이 지원
 public void afterPropertiesSet() throws Exception{

 }

 @Override
 // DisposableBean이 지원
 public void destroy() throws Exception{

 }
}
```

> - 인터페이스 단점
>   - 스프링 전용 인터페이스 의존적.
>   - 초기화, 소멸 메서드의 이름 변경 X (오버라이딩)
>   - 외부 라이브러리 적용 X

</br>

- 설정 정보에 초기화 메서드, 종료 메서드 지정

@Bean(initMethod = "methodName", destroyMethod = "methodName2")

> - 메서드 이름 설정에대한 자유
> - 스프링 코드에 의존 X
> - 설정 정보 사용 -> 외부라이브러리 적용 가능

> - 종료메서드 추론
>   - destroyMethod default = (infereed)
>   - 자동으로 close, shutdown 자동 초훌
>   - 추론 기능 사용하기 싫으면, destroyMethod=""

</br>

- @PostConstruct, @PreDestroy (`권장`)

```java
 public class Name{

   @PostConstruct
   public void init(){

   }

   @PreDestroy
   public void close(){

   }
 }

```

> - 최신 스프링 권장 방법
> - JSR-250 자바 표준
> - ComponentScan과 적합함
> - 외부 라이브러리 적용 X , 그럴때는 @Bean 기능 사용
