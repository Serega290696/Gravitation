package being.mathlab.expressions.types;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.PolynomialExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MultipleExpression extends PolynomialExpression {

    public MultipleExpression(Expression... _members) {
        for (int i = 0; i < _members.length; i++) {
            if(_members[i] instanceof NumericExpression
            && ((NumericExpression)_members[i]).getValue() == 0) {
                _members = new Expression[1];
                _members[0] = new NumericExpression(0);
                break;
            }
        }
        this.members = new ArrayList<>(Arrays.asList(_members));
    }

    @Override
    public double value(double... vars) {
        double result = 1;
        for (Expression member : members) {
            result *= member.value(vars);
        }
        return result;
    }


    @Override
    public Expression replaceVariableBy(Map<Integer, NumericExpression> vars) {
        Expression[] expressions2 = new Expression[members.size()];
        for (int i = 0; i < members.size(); i++) {
            expressions2[i] = members.get(i).replaceVariableBy(vars);
            if (expressions2[i] instanceof NumericExpression
                    && ((NumericExpression)expressions2[i]).getValue() == 0) {
                return new NumericExpression(0);
            }
        }
        return new MultipleExpression(expressions2);
    }

    @Override
    public LexemType getLexemType() {
        return LexemType.MULTIPLY;
    }
}
