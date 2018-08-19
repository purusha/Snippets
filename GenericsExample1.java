import java.util.ArrayList;
import java.util.List;

public class GenericsExample1 {
    public static void main(String[] args) {
        
        /* 

            arrays are covariant ... but generics are not !!?

            java.lang.Object
                java.lang.Number
                    java.lang.Integer
                     
         */
        print(new Integer[]{1, 2, 3});
        
        List<Integer> data2 = new ArrayList<Integer>();
        data2.add(42);
        data2.add(43);
        data2.add(44);        
        //print(data2); //compile error
    }
    
    static void print(List<Number> data) {
        for (Number n : data) {
            System.out.println(n);
        }        
    }

    static void print(Number[] data) {
        for (Number n : data) {
            System.out.println(n);
        }
    }
}