package being.mathlab.expressions.types;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.PolynomialExpression;

import java.util.Map;

public class MinusExpression extends PolynomialExpression {

    public MinusExpression(Expression... members) {
        super(members);
    }

    @Override
    public double value(double... vars) {
        double result = vars[0];
        for (int i = 1; i < members.size(); i++) {
            Expression member = members.get(i);
            result -= member.value(vars);
        }
        return result;
    }

    @Override
    public Expression replaceVariableBy(Map<Integer, NumericExpression> vars) {
        Expression[] expressions2 = new Expression[members.size()];
        for (int i = 0; i < members.size(); i++) {
            expressions2[i] = members.get(i).replaceVariableBy(vars);
        }
        return new MinusExpression(expressions2);
    }


    @Override
    public LexemType getLexemType() {
        return LexemType.MINUS;
    }
}
