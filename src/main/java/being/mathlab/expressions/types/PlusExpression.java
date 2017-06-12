package being.mathlab.expressions.types;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.PolynomialExpression;

import java.util.Map;

public class PlusExpression extends PolynomialExpression {

    public PlusExpression(Expression... members) {
        super(members);
    }

    @Override
    public double value(double... vars) {
        double sum = 0;
        for (Expression member : members) {
            sum += member.value(vars);
        }
        return sum;
    }

    @Override
    public Expression replaceVariableBy(Map<Integer, NumericExpression> vars) {
        Expression[] expressions2 = new Expression[members.size()];
        for (int i = 0; i < members.size(); i++) {
            expressions2[i] = members.get(i).replaceVariableBy(vars);
        }
        return new PlusExpression(expressions2);
    }

    @Override
    public LexemType getLexemType() {
        return LexemType.PLUS;
    }
}
