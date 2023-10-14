package FrontEnd.ErrorManager;

import java.util.ArrayList;

public class ErrorChecker {
    private static final ArrayList<Error> errors = new ArrayList<>();

    public static void AddError(Error error) {
        errors.add(error);
    }

    public static void showErrorMsg() {
        errors.sort(Error::compareTo);
        for (Error e : errors) {
            System.out.println(e);
        }
    }
}
