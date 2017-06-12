package being.mathlab.expressions;

import com.sun.istack.internal.NotNull;
import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.expressions.types.VariableExpression;
import being.mathlab.utils.ExpressionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class PolynomialExpression implements Expression {

    protected List<Expression> members = new ArrayList<>();

    public PolynomialExpression(Expression... members) {
        this.members = new ArrayList<>(Arrays.asList(members));
    }

    public void addMember(@NotNull Expression member) {
        if(member == null) {
            throw new IllegalArgumentException();
        }
        members.add(member);
    }

    public Set<Integer> variables() {
        return ExpressionUtil.extractVarsFromExpression(members.toArray(new Expression[members.size()]));
    }

    public int size() {
        return members.size();
    }

    public List<Expression> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        StringBuilder fullExpression = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            Expression member = members.get(i);
            if(member == null) {
                throw new NullPointerException("Member #" + i + " is null. Other part of expression: " + fullExpression);
            }
            if (!(member instanceof NumericExpression)
                    && !(member instanceof VariableExpression)) {
                fullExpression.append("(").append(member.toString()).append(")");
            } else {
                fullExpression.append(member.toString());
            }
            if (i + 1 < members.size()) {
                fullExpression.append(" ").append(getOperationSymbol()).append(" ");
            }
        }
        return fullExpression.toString();
    }
}
