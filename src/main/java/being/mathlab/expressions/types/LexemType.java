package being.mathlab.expressions.types;

public enum LexemType {
    VARIABLE("x", "x", 0),
    NUMERIC("", "", 0),
    PLUS("+", "plus", 1),
    MINUS("-", "minus", 1),
    MULTIPLY("*", "multiply", 2),
    DIVIDE("/", "divide", 2),
    L_BRACKET("(", "bracket", 3),
    R_BRACKET(")", "bracket", 3),
    POWER("^", "power", 3),
    EXP("exp", "exp", 3),
    SIN("sin", "sin", 3),
    COS("cos", "cos", 3),
    TAN("tan", "tan", 3),
    CTG("ctg", "ctg", 3),
    SQR("sqr", "sqr", 3),
    ABS("abs", "abs", 3),
    LN("ln", "ln", 3);

    private final String opSymbol;
    private final String opName;
    private final int opPriority;

    LexemType(String opSymbol, String opName, int opPriority) {
        this.opSymbol = opSymbol;
        this.opName = opName;
        this.opPriority = opPriority;
    }

    public String getOpSymbol() {
        return opSymbol;
    }

    public String getOpName() {
        return opName;
    }

    public int getOpPriority() {
        return opPriority;
    }
}
