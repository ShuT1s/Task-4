import java.util.Random;
import java.util.function.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

// 1. Лямбда для интерфейса Printable
interface Printable
{
    void print();
}

// 2. Проверка строки
public class LambdaTask
{
    public static void main(String[] args)
    {
        // 1. Лямбда для Printable
        Printable printable = () -> System.out.println("Printing...");
        printable.print();

        // 2. Проверка строки
        String testString = "Hello World";

        Predicate<String> notNull = s -> s != null;
        Predicate<String> notEmpty = s -> !s.isEmpty();

        System.out.println("Строка не null: " + notNull.test(testString));
        System.out.println("Строка не пуста: " + notEmpty.test(testString));
        System.out.println("Строка не null и не пуста: " + (notNull.and(notEmpty).test(testString)));

        // 3. Проверка строки на начало с 'J' и конец на 'A'
        Predicate<String> startsWithJEndsWithA = s -> s != null && s.startsWith("J") && s.endsWith("A");
        System.out.println("Строка начинается с 'J' и заканчивается на 'A': " + startsWithJEndsWithA.test("Java"));

        // 4. Лямбда для HeavyBox
        class HeavyBox
        {
            private int weight;

            public HeavyBox(int weight)
            {
                this.weight = weight;
            }
        }

        Consumer<HeavyBox> printWeight = box -> System.out.println("Отгрузили ящик с весом " + box.weight);
        printWeight.accept(new HeavyBox(10));
        
        // 5. Лямбда для Function
        Function<Integer, String> numberToString = num ->
        {
            if (num > 0)
            {
                return "Положительное число";
            }
            else if (num < 0)
            {
                return "Отрицательное число";
            }
            else
            {
                return "Ноль";
            }
        };

        System.out.println("Число 5: " + numberToString.apply(5));
        System.out.println("Число -3: " + numberToString.apply(-3));
        System.out.println("Число 0: " + numberToString.apply(0));

        // 6. Лямбда для Supplier
        Supplier<Integer> randomNum = () -> new Random().nextInt(11); // 0-10 включительно
        System.out.println("Случайное число от 0 до 10: " + randomNum.get());

        // Работа с аннотациями и рефлексией

        processAnnotations(LambdaTask.class); // Пример использования на текущем классе
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface DeprecatedEx
    {
        String message();
    }

    private static void processAnnotations(Class<?> clazz)
    {
        for (Method method : clazz.getDeclaredMethods())
        {
            DeprecatedEx annotation = method.getAnnotation(DeprecatedEx.class);
            if (annotation != null)
            {
                System.out.println("Внимание: метод " + method.getName() + " устарел. Альтернатива: " + annotation.message());
            }
        }

        for (Field field : clazz.getDeclaredFields())
        {
            DeprecatedEx annotation = field.getAnnotation(DeprecatedEx.class);
            if (annotation != null)
            {
                System.out.println("Внимание: класс " + clazz.getName() + " устарел. Альтернатива: " + annotation.message());
            }
        }
    }
}
