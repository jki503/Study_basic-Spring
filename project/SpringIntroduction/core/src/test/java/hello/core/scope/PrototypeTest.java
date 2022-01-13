package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.*;

public class PrototypeTest {

    @Test
    public void prototypeBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        //이 부분에 class 지정하면 그 대상이 componentScan 대상 지정
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); //이때 객체생성
        System.out.println("find prototypeBean1");

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); //이때 객체 생성
        System.out.println("find prototypeBean2");

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        ac.close();

    }

    @Scope("prototype")
    static class PrototypeBean{

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy(){ // 실행 X
            System.out.println("PrototypeBean.destroy");
        }
    }
}
