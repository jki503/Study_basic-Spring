package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @Commponent 붙은 것들을 자동으로 bean에 등록
@ComponentScan (
        // 탐색 패키지 시작 위치 지정
        basePackages = "hello.core.member",
        basePackageClasses = AutoAppConfig.class,
        // 수동 등록한 것들과 충돌 피하기 위해.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {


}
