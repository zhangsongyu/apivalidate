import lombok.Data;

import java.util.List;

@Data
public class Persion {
    Integer id;
    String name;
    Integer age;
    List<String> alias;
    Child child;
    List<Child> childList;


}
