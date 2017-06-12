package being.mathlab.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MathUtil {

    public static double round(double value, int numbersAmountAfterDot) {
        return (double) Math.round(value * Math.pow(10, numbersAmountAfterDot))
                / Math.pow(10, numbersAmountAfterDot);
    }

    public static BigDecimal round(BigDecimal value, int numbersAmountAfterDot) {
        return BigDecimal.valueOf(
                (double) Math.round(value.doubleValue() * Math.pow(10, numbersAmountAfterDot))
                        / Math.pow(10, numbersAmountAfterDot)
        );
    }

    public static BigDecimal round(Number value, int numbersAmountAfterDot) {
        return BigDecimal.valueOf(
                (double) Math.round(value.doubleValue() * Math.pow(10, numbersAmountAfterDot))
                        / Math.pow(10, numbersAmountAfterDot)
        );
    }

    public static boolean isNumber(char nextChar) {
        return NumberUtils.isNumber(String.valueOf(nextChar));
    }

    public static double min(double... values) {
        double lastMinimum = Double.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < lastMinimum) {
                lastMinimum = values[i];
            }
        }
        return lastMinimum;
    }

    public static double min(List<Double> values) {
        double lastMinimum = Double.MAX_VALUE;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) < lastMinimum) {
                lastMinimum = values.get(i);
            }
        }
        return lastMinimum;
    }

    public static double keyByMinValue(Map<Double, Double> values) {
        double key = Double.MAX_VALUE;
        double lastMinimum = Double.MAX_VALUE;
        for (Double d : values.keySet()) {
            if (values.get(d) <= lastMinimum) {
                key = d;
                lastMinimum = values.get(d);
            }
        }
        return key;
    }
}
