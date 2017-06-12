package being.mathlab.matrix;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.types.MultipleExpression;
import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.expressions.types.PlusExpression;
import being.mathlab.expressions.types.VariableExpression;

import java.util.Map;

public class Matrix {

    private int width;
    private int height;
    private Expression[][] matrixArray;

    public Matrix(int width, int height) {
        matrixArray = new Expression[height][width];
        this.width = width;
        this.height = height;
    }

    public Matrix(Expression[][] twoDimensionalArray) {
        this.height = twoDimensionalArray.length;
        this.width = twoDimensionalArray[0].length;
        matrixArray = new Expression[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                set(i, j, twoDimensionalArray[i][j]);
            }
        }
    }

    public void addCol() {
        width++;
        Expression[][] tmp = new Expression[matrixArray.length][matrixArray[0].length + 1];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i][0] = new NumericExpression(1);
        }
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length - 1; j++) {
                tmp[i][j + 1] = matrixArray[i][j];
            }
        }
        matrixArray = tmp;
    }

    public <T extends Number> Matrix(double[][] twoDimensionalArray) {
        this.height = twoDimensionalArray.length;
        this.width = twoDimensionalArray[0].length;
        matrixArray = new Expression[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                set(i, j, new NumericExpression(twoDimensionalArray[i][j]));
            }
        }
    }

    public <T extends Number> Matrix(Object[][] twoDimensionalArray) throws Exception {
        this.height = twoDimensionalArray.length;
        this.width = twoDimensionalArray[0].length;
        matrixArray = new Expression[height][width];
        if ((twoDimensionalArray[0][0] instanceof Number)) {
            throw new Exception("Wrong matrixArray element type");
        }
        Double[][] array = (Double[][]) twoDimensionalArray;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                set(i, j, new NumericExpression(array[i][j].doubleValue()));
            }
        }
    }

    public static Matrix newUpperTriangular(int width, int height) {
        return newLowerTriangular(width, height, 0).transpose();
    }

    public static Matrix newUpperTriangular(int width, int height, int varsPortion) {
        return newLowerTriangular(width, height, varsPortion).transpose();
    }

    public static Matrix newLowerTriangular(int width, int height) {
        return newLowerTriangular(width, height, 0);
    }

    public static Matrix newLowerTriangular(int width, int height, int varsPortion) {
        Matrix matrix = new Matrix(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i < j) {
                    matrix.set(i, j, new NumericExpression(0d));
                } else {
                    matrix.set(i, j, new VariableExpression((int) (100 * varsPortion + (i + 1) * 10 + j + 1)));
                }
            }
        }
        return matrix;
    }

    public void set(int i, int j, Expression expression) {
        matrixArray[i][j] = expression;
    }

    public Expression get(int i, int j) {
        return matrixArray[i][j];
    }

    public Matrix setValue(Map<Integer, NumericExpression> vars) {
//        //System.out.println("Matrix.setValue");
        Matrix matrix = new Matrix(this.matrixArray);
//        //System.out.println("this = " + matrix);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Expression expression = matrix.get(i, j).replaceVariableBy(vars);
                if (expression.variables().size() == 0) {
                    matrix.set(i, j, new NumericExpression(expression.value()));
                } else {
                    matrix.set(i, j, expression);
                }
            }
        }
        return matrix;
    }

    public Matrix transpose() {
        //System.out.println("Matrix.transpose");
        //System.out.println("Original:");
        //System.out.println(this);
        Matrix transposed = new Matrix(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                transposed.set(j, i, get(i, j));
            }
        }
        //System.out.println("Transposed:");
        //System.out.println(transposed);
        return transposed;
    }

    public Matrix multiply(Matrix multiplicand) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(this.get(i, j) == null) {
                    try {
                        throw new Exception("Can't multiply. Here is null: " + i + "x" + j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (int i = 0; i < multiplicand.getHeight(); i++) {
            for (int j = 0; j < multiplicand.getWidth(); j++) {
                if(multiplicand.get(i, j) == null) {
                    try {
                        throw new Exception("Can't multiply. Here is null: " + i + "x" + j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Matrix multiplication = new Matrix(multiplicand.getWidth(), height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < multiplicand.getWidth(); j++) {
                Expression[] sum = new Expression[width];
                for (int k = 0; k < width; k++) {
                    sum[k] = new MultipleExpression(get(i, k), multiplicand.get(k, j));
                }
                PlusExpression plusExpression = new PlusExpression(sum);
                if (plusExpression.variables().size() == 0) {
                    NumericExpression numericExpression = new NumericExpression(plusExpression.value());
                    multiplication.set(i, j,
                            numericExpression
                    );
                } else {
                    multiplication.set(i, j,
                            plusExpression
                    );
                }
            }
        }
        //System.out.println("Multiplication:");
        //System.out.println(multiplication);
        return multiplication;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Matrix ("+width+", "+height+")");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                string.append("[").append(get(i, j)).append("] ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}