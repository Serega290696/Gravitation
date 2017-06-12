package being.mathlab.expressions;

import being.mathlab.expressions.types.LexemType;
import being.mathlab.expressions.types.NumericExpression;

import java.util.Map;
import java.util.Set;

public interface Expression {

//    default double value() {
//        //todo: remove value(double... vars); and rewrite method
//        return value(0);
//    }

    double value(double... vars);

    Expression replaceVariableBy(Map<Integer, NumericExpression> vars);

    default String getOperationName() {
        return getLexemType().getOpName();
    }

    default String getOperationSymbol() {
        return getLexemType().getOpSymbol();
    }

    default int getPriorityLevel() {
        return getLexemType().getOpPriority();
    }

//    default double derivative(Map<Integer, NumericExpression> point) {
//        double delta = 0.001;
//        Map<Integer, NumericExpression> point2 = new HashMap<>(point);
//        for (Map.Entry<Integer, NumericExpression> entry : point.entrySet()) {
//            point2.put(entry.getKey(), new NumericExpression(entry.getValue().value().doubleValue() + delta));
//        }
//        return (value(point) - value(point2)) / delta;
//    }

    Set<Integer> variables();

    LexemType getLexemType();


}
