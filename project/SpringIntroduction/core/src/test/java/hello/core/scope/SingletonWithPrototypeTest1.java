package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    public void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        //이 부분에 class 지정하면 그 대상이 componentScan 대상 지정
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); //이때 객체생성
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();

        assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    static class ClientBean{
        /*
        * 스프링 빈이 싱글톤 clientBean 등록하고 생성자에서 의존성 주입할때
        * 스프링이 내부적으로 프로토타입빈을 만든 후에 던져줌
        * 그때 만들어진 프로타입 빈은 할당됨
        * 즉 생성시점에 주입, 그래서 계속 같은 프로타입 빈을 사용.
        * */
/*
        private final PrototypeBean prototypeBean;



        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }
*/

        /*
        * getObject 호출하면 그때서야 스프링 컨테이너에서 프로토타입빈을 찾아서 반환
       * */
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;
//        private ObjectFactory<PrototypeBean> prototypeBeanProvider;

        /*
        public int logic(){
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }*/

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean{

        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy(){ // 실행 X
            System.out.println("PrototypeBean.destroy");
        }
    }
}
