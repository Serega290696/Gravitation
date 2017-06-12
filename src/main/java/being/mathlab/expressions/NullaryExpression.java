package being.mathlab.expressions;

public abstract class NullaryExpression implements Expression {


    public NullaryExpression(Number value) {
        setValue(value);
    }


    @Override
    public String toString() {
        String varIndex = String.valueOf(value());
        return getOperationName() + varIndex;
    }

    public abstract double value();

    public abstract Number getValue();

    public abstract void setValue(Number number);
}
