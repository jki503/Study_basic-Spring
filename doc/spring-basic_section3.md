---
title: "스프링 핵심 원리 이해2 - 객체 지향 원리 적용"
category: basic-Spring
tags: [Spring Container, Spring Bean]
Author: Jung
---

## AppConfig

> - 애플리케이션 전체 동작 방식 구성하기 위한 `구현 객체 생성 및 연결하는 책임`을 가지는 별도의 설정 클래스!

> - 사용 영역과, 객체를 생성하고 구성하는 영역으로 분리

## IoC

> - but 프로그램에 대한 모든 권한을 `AppCofig`가 가짐
> - 즉 프로그램의 제어 흐름을 직접 관리하는 것이 아닌 외부에서 관리하는 것

## IoC 컨테이너, DI 컨테이너

> - AppConfig처럼 객체를 생성하고 관리하며 의존 관계를 연결 해주는 것
