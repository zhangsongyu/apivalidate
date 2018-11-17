import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortTest {

    @Test
    public void ttttt() {
        @Data
        class P {
            Integer id;
            String name;
            Integer age;
            String alias;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                P p = (P) o;
                return Objects.equals(name, p.name) &&
                        Objects.equals(age, p.age);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, age);
            }

            public P(Integer id, String name, Integer age, String alias) {
                this.id = id;
                this.name = name;
                this.age = age;
                this.alias = alias;
            }
        }


        Stream<P> ss = Stream.of(
                new P(0, "a", 1, "la3dla"),
                new P(3, "a", 1, "lasdla"),
                new P(4, "b", 3, "lasfdsla"),
                new P(2, "c", 5, "lsfala"),
                new P(1, "c", 5, "lavsvsla")
        );

      List<P> res=  ss.sorted(Comparator.comparing(P::getId).reversed()).distinct().collect(Collectors.toList());
        System.out.println();
    }
}
