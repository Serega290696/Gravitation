package being.mathlab.expressions.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import being.mathlab.expressions.Expression;
import being.mathlab.expressions.types.*;
import being.mathlab.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {

    private static int printLvl = 0;
    private static String indentSymbol = "   ";

    private static void print(String msg) {
        for (int i = 0; i < printLvl; i++) {
            msg = indentSymbol + "|" + msg;
        }
//        MathSystem.out.println(msg);
    }

    public static Expression parse(String stringExpression) {
        printLvl++;
        stringExpression = stringExpression.replaceAll("\\s+", "");
        print("BEGIN[" + stringExpression + "]");
        List<Integer> plusOrMinusPosition = new ArrayList<>();
        List<Integer> complexOperatorPosition = new ArrayList<>();
        List<Integer> bracketsBlocksPosition = new ArrayList<>();
        int lvl = 0;
        // find brackets
        for (int i = 0; i < stringExpression.length(); i++) {
            if (stringExpression.charAt(i) == '(') {
                lvl++;
            } else if (stringExpression.charAt(i) == ')') {
                lvl--;
            }
            if (lvl > 0 && bracketsBlocksPosition.size() % 2 == 0) {
                bracketsBlocksPosition.add(i);
            } else if (lvl == 0 && bracketsBlocksPosition.size() % 2 == 1) {
                bracketsBlocksPosition.add(i);
            }
        }
        if (bracketsBlocksPosition.size() == 2 && bracketsBlocksPosition.get(0) == 0
                && bracketsBlocksPosition.get(1) == stringExpression.length() - 1) {
            bracketsBlocksPosition.clear();
            stringExpression = stringExpression.substring(1, stringExpression.length() - 1);
            for (int i = 0; i < stringExpression.length(); i++) {
                if (stringExpression.charAt(i) == '(') {
                    lvl++;
                } else if (stringExpression.charAt(i) == ')') {
                    lvl--;
                }
                if (lvl > 0 && bracketsBlocksPosition.size() % 2 == 0) {
                    bracketsBlocksPosition.add(i);
                } else if (lvl == 0 && bracketsBlocksPosition.size() % 2 == 1) {
                    bracketsBlocksPosition.add(i);
                }
            }
        }
        // find + and -
        boolean hidden = false;
        for (int i = 0; i < stringExpression.length(); i++) {
            if (bracketsBlocksPosition.contains(i)) {
                hidden = !hidden;
            } else if (!hidden) {
                if (stringExpression.charAt(i) == '+'
                        || (stringExpression.charAt(i) == '-' && (i == 0 || stringExpression.charAt(i-1) != 'e'))) {
                    plusOrMinusPosition.add(i);
                }
            }
        }
        // find *
        hidden = false;
        for (int i = 0; i < stringExpression.length(); i++) {
            if (bracketsBlocksPosition.contains(i)) {
                hidden = !hidden;
            } else if (!hidden) {
                if (stringExpression.charAt(i) == '*'
                        || stringExpression.charAt(i) == '/'
                        || stringExpression.charAt(i) == '^') {
                    complexOperatorPosition.add(i);
                }
            }
        }

        if (plusOrMinusPosition.size() > 0) {
            if (plusOrMinusPosition.get(0) != 0) {
                plusOrMinusPosition.add(0, -1);
            }
        } else {
            plusOrMinusPosition.add(0, -1);
        }
        if (plusOrMinusPosition.size() >= 2 && plusOrMinusPosition.get(plusOrMinusPosition.size() - 1) != stringExpression.length() - 1) {
            plusOrMinusPosition.add(stringExpression.length());
        } else {
            plusOrMinusPosition.add(stringExpression.length());
        }
        PlusExpression plusExpression = new PlusExpression();
        Expression subExpression;
        String subString;
        for (int i = 0; i + 1 < plusOrMinusPosition.size(); i++) {
            subString = stringExpression.substring(
                    plusOrMinusPosition.get(i) + 1,
                    plusOrMinusPosition.get(i + 1)
            );
            print("#" + i + ": stringExpression :: " + subString);
            subExpression = null;
            Expression leftExpression = null;
            for (int j = 0; j < complexOperatorPosition.size(); j++) {
                if (complexOperatorPosition.get(j) > plusOrMinusPosition.get(i) + 1
                        && complexOperatorPosition.get(j) < plusOrMinusPosition.get(i + 1)) {
                    int endIndex;
                    if (j + 1 == complexOperatorPosition.size()) {
                        endIndex = plusOrMinusPosition.get(i + 1);
                    } else {
                        endIndex = Math.min(
                                complexOperatorPosition.get(j + 1),
                                plusOrMinusPosition.get(i + 1)
                        );
                    }
                    for (int k = 0; k < complexOperatorPosition.size(); k++) {
                        print("K: " + complexOperatorPosition.get(k));
                    }
                    print("" + (plusOrMinusPosition.get(i + 1)));
                    print("" + complexOperatorPosition.get(j));
                    print("" + endIndex);
                    print("Create MultipleExpression: " + stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex));
                    if (stringExpression.charAt(complexOperatorPosition.get(j)) == '*') {

                        if (leftExpression != null) {
                            subExpression = new MultipleExpression(
                                    leftExpression,
                                    parse(stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex))
                            );
                        } else {
                            subExpression = new MultipleExpression(
                                    parse(stringExpression.substring(plusOrMinusPosition.get(i) + 1,
                                            complexOperatorPosition.get(j))),
                                    parse(stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex))
                            );
                        }
                    } else if (stringExpression.charAt(complexOperatorPosition.get(j)) == '/') {

                        if (leftExpression != null) {
                            subExpression = new DivideExpression(
                                    leftExpression,
                                    parse(stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex))
                            );
                        } else {
                            subExpression = new DivideExpression(
                                    parse(stringExpression.substring(plusOrMinusPosition.get(i) + 1,
                                            complexOperatorPosition.get(j))),
                                    parse(stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex))
                            );
                        }
                    } else if (stringExpression.charAt(complexOperatorPosition.get(j)) == '^') {

                        if (leftExpression != null) {
                            subExpression = new PowerExpression(
                                    leftExpression,
                                    parse(stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex))
                            );
                        } else {
                            subExpression = new PowerExpression(
                                    parse(stringExpression.substring(plusOrMinusPosition.get(i) + 1,
                                            complexOperatorPosition.get(j))),
                                    parse(stringExpression.substring(complexOperatorPosition.get(j) + 1, endIndex))
                            );
                        }
                    }

                    leftExpression = subExpression;
                }
            }
            if (subExpression == null) {
                subExpression =
                        subExpressionParse(subString, null);
                print("Create " + subExpression.getLexemType());
            }
            if (plusOrMinusPosition.get(i) >= 0 && plusOrMinusPosition.get(i) < stringExpression.length()
                    && stringExpression.charAt(plusOrMinusPosition.get(i)) == '-') {
                subExpression = new UnarMinusExpression(subExpression);
            }
            plusExpression.addMember(
                    subExpression
            );
        }
        Expression e = plusExpression;
        if (plusExpression.size() == 1) {
            e = plusExpression.getMembers().get(0);
        }
        print("END[" + plusExpression + "]");
        printLvl--;
        return e;
    }

    static class ExpressionParsingReport {
        private Expression expression;
        private String restStringExpression;

        public ExpressionParsingReport(Expression expression, String restStringExpression) {
            this.expression = expression;
            this.restStringExpression = restStringExpression;
        }

        public Expression getExpression() {
            return expression;
        }

        public String getRestStringExpression() {
            return restStringExpression;
        }
    }

    public static Expression subExpressionParse(String stringExpression, Expression leftExpression) {
        printLvl++;
        stringExpression = stringExpression.replaceAll("\\s+", "");
        String newStringExpression = stringExpression;
        print("BEGIN_sub[" + stringExpression + "]");
        Expression currentExpression = null;
        char c = stringExpression.charAt(0);
        if (NumberUtils.isNumber(String.valueOf(c))
                || (c == '-' && leftExpression == null)) {
            boolean creatable = true;
            int stringLength = 1;
            String substring = stringExpression.substring(0, stringLength);
            int power = 0;
            while (stringLength < stringExpression.length() && creatable) {
                stringLength++;
                substring = stringExpression.substring(0, stringLength);
                creatable = NumberUtils.isNumber(substring);
                if (substring.charAt(substring.length() - 1) == 'e') {
                    substring = substring.substring(0, substring.length() - 1);
                    String powerStr = "";
                    int firstSymbol = stringLength;
                    int powerStringLength = 1;
                    if (stringExpression.charAt(firstSymbol) == '-') {
                        powerStr = "-";
                        powerStringLength++;
                    }
                    boolean creatablePower = true;
                    while (firstSymbol +powerStringLength <= stringExpression.length() && creatablePower) {
                        powerStr = stringExpression.substring(firstSymbol, firstSymbol + powerStringLength);
                        creatablePower = NumberUtils.isNumber(powerStr);
                        powerStringLength++;
                    }
                    power = Integer.valueOf(powerStr);
                }
            }
            if (stringLength > stringExpression.length()) {
                stringLength--;
            } else if (!creatable) {
                stringLength--;
            }
            substring = stringExpression.substring(0, stringLength);
            currentExpression = new NumericExpression(Double.parseDouble(substring) * Math.pow(10, power));
            if (stringLength <= stringExpression.length()) {
                newStringExpression = stringExpression.substring(stringLength);
            } else {
                newStringExpression = "";
            }
        } else if (c == '+') {
            newStringExpression = stringExpression.substring(1);
            currentExpression = new PlusExpression(leftExpression, subExpressionParse(stringExpression.substring(1), null));
        } else if (c == '-') {
            newStringExpression = stringExpression.substring(1);
            currentExpression = new MinusExpression(leftExpression, subExpressionParse(stringExpression.substring(1), null));
        } else if (c == '*') {
            newStringExpression = stringExpression.substring(1);
            currentExpression = new MultipleExpression(leftExpression, subExpressionParse(stringExpression.substring(1), null));
        } else if (c == '/') {
            newStringExpression = stringExpression.substring(1);
            currentExpression = new DivideExpression(leftExpression, subExpressionParse(stringExpression.substring(1), null));
        } else if (c == '(') {
            newStringExpression = stringExpression.substring(1, stringExpression.indexOf(')') + 1);
            currentExpression = parse(newStringExpression);//new PlusExpression(leftExpression, newStringExpression);
        } else if (c == '^') {
            newStringExpression = stringExpression.substring(1);
            currentExpression = new PowerExpression(leftExpression, subExpressionParse(stringExpression.substring(1), null));
        } else if (c == 'e') {
//            print(stringExpression);
            newStringExpression = stringExpression.substring(1);
//            if (leftExpression == null) {
            currentExpression = new ExpExpression(subExpressionParse(stringExpression.substring(1), null));
//            } else {
//                currentExpression = new ExpExpression(subExpressionParse(stringExpression.substring(1), null));
//            }
        } else if (c == 'x') {
            newStringExpression = stringExpression.substring(1);
            char nextChar;
            String number = "";
            for (int i = 0; i < newStringExpression.length(); i++) {
                nextChar = newStringExpression.charAt(i);
                if (MathUtil.isNumber(nextChar)) {
                    number = number + nextChar;
                } else {
                    break;
                }
            }
            if (StringUtils.isEmpty(number)) {
                System.out.println(" [WARN] 'x' in expression without index");
                number = "1";
            }
            System.out.println(number);
            currentExpression = new VariableExpression(Integer.parseInt(number));
        } else if (c == 'y') {
//            print(stringExpression);
            newStringExpression = stringExpression.substring(1);
            currentExpression = new VariableExpression(2);
        } else if (c == 'z') {
//            print(stringExpression);
            newStringExpression = stringExpression.substring(1);
            currentExpression = new VariableExpression(3);
        } else if (stringExpression.length() >= 3) {
            if (stringExpression.substring(0, 3).equals("sin")) {
                currentExpression = new SinExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("cos")) {
                currentExpression = new CosExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("tan")) {
                currentExpression = new TanExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("ctg")) {
                currentExpression = new CtgExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("sqr")) {
                currentExpression = new SqrExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("exp")) {
                currentExpression = new ExpExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("abs")) {
                currentExpression = new AbsExpression(subExpressionParse(stringExpression.substring(3), null));
            } else if (stringExpression.substring(0, 3).equals("log")) {
                currentExpression = new LogExpression(subExpressionParse(stringExpression.substring(3), null));
            }
        }

        if (currentExpression == null) {
            throw new NullPointerException("Invalid expression");
        }
        printLvl--;
        return currentExpression;
    }

}
