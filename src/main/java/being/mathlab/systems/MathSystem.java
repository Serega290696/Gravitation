package being.mathlab.systems;

import being.mathlab.equations.Equation;
import being.mathlab.equations.impl.EquationSolver;
import being.mathlab.expressions.types.NumericExpression;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MathSystem {
    private Set<Equation> equations = new HashSet<>();
    private EquationSolver solver = new EquationSolver();

    public MathSystem(Set<Equation> equations) {
        this.equations = equations;
    }

    public Map<Integer, NumericExpression> solve() {
        Set<Integer> vars = new HashSet<>();
        for (Equation eq : equations) {
            vars.addAll(eq.variables());
        }
        Map<Integer, NumericExpression> finalSolution = new HashMap<>();
        for (int i = 0; i < vars.size(); i++) {
            for (Equation equation : equations) {
                if (solver.isSolverable(equation)) {
//                    System.out.println("Is solverable: " + equation);
//                    try {
                    Map<Integer, NumericExpression> solution = equation.solve();
                    finalSolution.putAll(solution);
                    Set<Equation> newEquations = new HashSet<>();
                    for (Equation curEquation : equations) {
                        newEquations.add(curEquation.replaceVar(solution));
                    }
                    equations = newEquations;
//                    System.out.println("#" + i);
//                    System.out.println(equations);
                    break;
//                    } catch (Exception e) {
//                        System.err.println(" [WARN] Cannot solve equation '" + equation + "' from system.");
//                    }
                }
//                else {
//                    System.out.println("equation = " + equation);
//                }
            }
        }
        return finalSolution;
    }


    public Set<Equation> getEquations() {
        return equations;
    }

    public void setEquations(Set<Equation> equations) {
        this.equations = equations;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Math system. Equations(" + equations.size() + "): \n");
        for (Equation equation : equations) {
            stringBuilder
                    .append(equation.getLeftExpression())
                    .append(" = ")
                    .append(equation.getRightExpression())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
