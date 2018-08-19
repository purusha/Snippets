import java.util.HashMap;
import java.util.Map;

public class GenericsExample2 {
    public static void main(String[] args) {

        final Mouse jerry = new Mouse();
        jerry.addFriend("spike", new Dog());
        jerry.addFriend("quacker", new Duck());        

        //type as parameter
        jerry.callFriend1("spike", Dog.class).bark();
        jerry.callFriend1("quacker", Duck.class).quack();
        
        //type without parameter #1
        jerry.<Dog>callFriend2("spike").bark();
        jerry.<Duck>callFriend2("quacker").quack();
        
        //type without parameter #2
        //jerry.callFriend3("spike").bark(); //compile error
        Dog dog = jerry.callFriend3("spike");
        dog.bark();
        
        Duck duck = jerry.callFriend3("spike");
        duck.quack();
        
    }

    public static class Mouse implements Animal {
        private Map<String, Animal> friends = new HashMap<>();
        
        public void addFriend(String string, Animal animal) {
            friends.put(string, animal);
        }
        
        public <T extends Animal> T callFriend1(String name, Class<T> type) {
            return type.cast(friends.get(name));
        }        
        
        @SuppressWarnings("unchecked")
        public <T extends Animal> T callFriend2(String name) {
            return (T) friends.get(name);
        }    
        
        @SuppressWarnings("unchecked")
        public <T> T callFriend3(String name){
            return (T) friends.get(name);
        }        
    }

    public static interface Animal {
    }

    public static class Duck implements Animal {
        public void quack() {
            System.out.println("quack");
        }
    }

    public static class Dog implements Animal {
        public void bark() {
            System.out.println("bark");
        }
    }

}
