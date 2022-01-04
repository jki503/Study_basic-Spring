package hello.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService(); // 클래스 레벨에 단 하나 존재 , static 공부

    public static SingletonService getInstance(){
        return instance;
    }

    private SingletonService(){}

    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }
}
