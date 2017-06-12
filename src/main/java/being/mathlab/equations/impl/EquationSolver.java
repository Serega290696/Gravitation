package being.mathlab.equations.impl;

import being.mathlab.equations.Equation;
import being.mathlab.expressions.Expression;
import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.expressions.types.PlusExpression;
import being.mathlab.expressions.types.UnarMinusExpression;
import being.mathlab.utils.MathUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EquationSolver {

    public Map<Integer, NumericExpression> solve(Equation equation) {
        Set<Integer> vars = equation.variables();
//        if (!isSolverable(equation)) {
//            throw new Exception("Wrong variables amount in equation");
//        }
        int var = vars.iterator().next();

        Expression newExpression = new PlusExpression(
                equation.getLeftExpression(),
                new UnarMinusExpression(equation.getRightExpression())
        );
        double valueInPoint;
        Map<Integer, NumericExpression> tmpVarsMap = new HashMap<>();
        Map<Double, Double> values = new HashMap<>();
        for (double j = 0; j < 1853630; j += Math.pow(j / 40, 2) + 0.05) {
            tmpVarsMap.clear();
            tmpVarsMap.put(var, new NumericExpression(j));
            valueInPoint = newExpression.replaceVariableBy(tmpVarsMap).value();
            values.put(j, Math.abs(valueInPoint));
            tmpVarsMap.clear();
            tmpVarsMap.put(var, new NumericExpression(-j));
            valueInPoint = newExpression.replaceVariableBy(tmpVarsMap).value();
            values.put(-j, Math.abs(valueInPoint));
        }
        double minimum = MathUtil.keyByMinValue(values);

        double intervalLength[] = {10, 0.5, 0.003};
        int deep = intervalLength.length;

        for (int i = 0; i < deep; i++) {
            values.clear();
            double stepSize = intervalLength[i] / 100;
            for (double j = minimum - intervalLength[i]/2;
                 j < minimum + intervalLength[i];
                 j += stepSize) {
                tmpVarsMap.clear();
                tmpVarsMap.put(var, new NumericExpression(j));
                valueInPoint = newExpression.replaceVariableBy(tmpVarsMap).value();
                values.put(j, Math.abs(valueInPoint));
                tmpVarsMap.clear();
                tmpVarsMap.put(var, new NumericExpression(-j));
                valueInPoint = newExpression.replaceVariableBy(tmpVarsMap).value();
                values.put(-j, Math.abs(valueInPoint));
            }
            minimum = MathUtil.keyByMinValue(values);
        }
        Map<Integer, NumericExpression> solution = new HashMap<>();
        solution.put(var, new NumericExpression(MathUtil.round(minimum, 4)));
        return solution;

    }

    public boolean isSolverable(Equation equation) {
        Set<Integer> vars = equation.variables();
        return vars.size() == 1;
    }

}
