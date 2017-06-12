package being.mathlab.expressions.types;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.UnaryExpression;

import java.util.Map;

public class TanExpression extends UnaryExpression {

    public TanExpression(Expression subExpression) {
        super(subExpression);
    }

    @Override
    public double value(double... vars) {
        return Math.tan(super.subExpression.value(vars));
    }

    @Override
    public Expression replaceVariableBy(Map<Integer, NumericExpression> vars) {
        return new TanExpression(super.subExpression.replaceVariableBy(vars));
    }

    @Override
    public LexemType getLexemType() {
        return LexemType.TAN;
    }
}
