package FrontEnd;

import Enums.ErrorType;

public class ErrorChecker {

    public static void AddError(int line, ErrorType type) {
        System.out.println(line + " : " + type);
    }
}
