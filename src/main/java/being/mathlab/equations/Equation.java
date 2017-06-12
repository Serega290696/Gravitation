package being.mathlab.equations;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.types.NumericExpression;

import java.util.Map;
import java.util.Set;

public interface Equation {

    Map<Integer, NumericExpression> solve();

    Equation simplify();

    Set<Integer> variables();


    void setLeftExpression(Expression leftExpression);

    void setRightExpression(Expression rightExpression);

    Expression getLeftExpression();

    Expression getRightExpression();

    Equation replaceVar(Map<Integer, NumericExpression> vars);

}
