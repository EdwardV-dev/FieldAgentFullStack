package learn.field_agent.domain;

public class Validations {

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank(); //returns true if value is null or blank
    }
}
