package being.mathlab.expressions.types;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.NullaryExpression;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableExpression extends NullaryExpression {

    public Integer value;

    public VariableExpression(double value) {
        super(value);
    }

    public Set<Integer> variables() {
        HashSet<Integer> set = new HashSet<>();
        set.add(getValue());
        return set;
    }

    @Override
    public double value(double... vars) {
        return vars[value-1];
    }

    @Override
    public Expression replaceVariableBy(Map<Integer, NumericExpression> vars) {
        if(vars.containsKey(value)) {
            return vars.get(value);
        }
        return new VariableExpression(value);
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Number number) {
        value = number.intValue();
    }

    @Override
    public LexemType getLexemType() {
        return LexemType.VARIABLE;
    }

}
