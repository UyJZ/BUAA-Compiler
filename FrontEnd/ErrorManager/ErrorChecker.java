package FrontEnd.ErrorManager;

import Enums.ErrorType;
import FrontEnd.Nodes.Node;
import FrontEnd.Nodes.TokenNode;

import java.io.PrintStream;
import java.util.ArrayList;

public class ErrorChecker {
    private static final ArrayList<Error> errors = new ArrayList<>();

    public static void AddError(Error error) {
        errors.add(error);
    }

    public static ArrayList<Error> getErrors() {
        return errors;
    }

    public static void showErrorMsg(PrintStream printStream) {
        errors.sort(Error::compareTo);
        for (Error e : errors) {
            printStream.println(e.toString());
        }
    }

    public static void AddError(ArrayList<Node> children, ErrorType errorType) {
        errors.add(new Error(children.get(children.size() - 1).getEndLine(), errorType));
    }
}
