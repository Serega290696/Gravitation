package being.mathlab.expressions;

import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.expressions.types.VariableExpression;
import being.mathlab.utils.ExpressionUtil;

import java.util.Set;

public abstract class UnaryExpression implements Expression {

    protected Expression subExpression;

    public UnaryExpression(Expression subExpression) {
        this.subExpression = subExpression;
    }

    public Set<Integer> variables() {
        return ExpressionUtil.extractVarsFromExpression(subExpression);
    }

    @Override
    public String toString() {
        String result;
        if ((!(subExpression instanceof NumericExpression)
                && !(subExpression instanceof VariableExpression))) {
            result = getOperationSymbol() + "(" + subExpression + ")";
        } else {
            result = getOperationSymbol() + subExpression;
        }
        return result;
    }

}
