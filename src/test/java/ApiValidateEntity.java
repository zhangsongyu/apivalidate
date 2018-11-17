import lombok.Data;

import java.util.List;

@Data
public class ApiValidateEntity {

    private String perfix;

    private String className;
    private List<String> classAnnotation;
    private String[] classMapping;
    private List<MethodEntity> methods;

    public ApiValidateEntity() {
    }

    public ApiValidateEntity(String perfix, String className, List<String> classAnnotation, String[] classMapping, List<MethodEntity> methods) {
        this.perfix = perfix;
        this.className = className;
        this.classAnnotation = classAnnotation;
        this.classMapping = classMapping;
        this.methods = methods;
    }
}
