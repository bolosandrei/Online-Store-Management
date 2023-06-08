package start;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionExample<T> {
    public static void retrieveColumnTitles(Object object) {
        // returneaza o lista de Object
        System.out.println("\n=========================================");
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                String format = "%1$-10s";
                value = field.get(object);
                System.out.format(format, field.getName());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n-----------------------------------------");
    }

    // tu trebuie sa primesti o lista de obiecte
    public static void retrieveProperties(Object object) {
// un for cu care parcurgi lista de obiecte
        // trebuie sa le salvezi intr-o matrice Object[][]
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                String format = "%1$-10s";
                value = field.get(object);
                System.out.format(format, value);
                //inserezi fiecare val in matr
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // returnezi mat
        System.out.println();
    }

    // new method returning a list of objects
    public List<T> retrieveColumnHeaders(List<T> object) {
        List<T> list = new ArrayList<>();
        T obj = object.get(0);
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                String format = "%1$-10s";
                value = field.getName();
                //System.out.format(format, value);
                list.add((T) value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}



