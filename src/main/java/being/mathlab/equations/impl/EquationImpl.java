package being.mathlab.equations.impl;

import being.mathlab.equations.Equation;
import being.mathlab.expressions.Expression;
import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.utils.ExpressionUtil;

import java.util.Map;
import java.util.Set;

public class EquationImpl implements Equation {

    private Expression leftExpression;
    private Expression rightExpression;
    private EquationSolver solver = new EquationSolver();

    public EquationImpl(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public Map<Integer, NumericExpression> solve() {
        return solver.solve(this);
    }

    @Override
    public Equation simplify() {
        return null;
    }

    @Override
    public Set<Integer> variables() {
        return ExpressionUtil.extractVarsFromExpression(leftExpression, rightExpression);
//        Set<Integer> vars = new HashSet<>();
//        for (VariableExpression var : leftExpression.variables()) {
//            if (!vars.contains(var.value())) {
//                vars.add(var.value());
//            }
//        }
//        return vars;
    }

    @Override
    public void setLeftExpression(Expression leftExpression) {
        this.leftExpression = leftExpression;
    }

    @Override
    public void setRightExpression(Expression rightExpression) {
        this.rightExpression = rightExpression;
    }

    @Override
    public Expression getLeftExpression() {
        return leftExpression;
    }

    @Override
    public Expression getRightExpression() {
        return rightExpression;
    }

    @Override
    public Equation replaceVar(Map<Integer, NumericExpression> vars) {
        return new EquationImpl(
                leftExpression.replaceVariableBy(vars),
                rightExpression.replaceVariableBy(vars)
        );
    }

    @Override
    public String toString() {
        return "Equation: " + leftExpression + " = " + rightExpression;
    }
}
