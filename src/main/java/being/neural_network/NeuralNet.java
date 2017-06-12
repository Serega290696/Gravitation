package being.neural_network;

import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.matrix.Matrix;

public class NeuralNet {
    private final int inputLayerSize;
    private final int outputLayerSize;
    private final Matrix[] weights;

    public NeuralNet(int hiddenLayersAmount, int inputLayerSize, int outputLayerSize,
                     int... hiddenLayersSizes) throws Exception {
        if (hiddenLayersAmount != hiddenLayersSizes.length) {
            throw new Exception("Weights matrix amount and hidden layers number don't match! "
                    + "Hidden layers amount: " + hiddenLayersAmount
                    + ". Weights matrix amount: " + hiddenLayersSizes.length);
        }
        this.inputLayerSize = inputLayerSize;
        this.outputLayerSize = outputLayerSize;
        weights = new Matrix[hiddenLayersAmount + 1];
        weights[0] = new Matrix(hiddenLayersSizes[0], inputLayerSize + 1);
        for (int i = 1; i < weights.length - 1; i++) {
            weights[i] = new Matrix(hiddenLayersSizes[i], hiddenLayersSizes[i - 1]);
        }
        weights[weights.length - 1] = new Matrix(outputLayerSize, hiddenLayersSizes[weights.length - 2] + 1);

        System.out.println("NeuralNet is created(" + inputLayerSize + ", " + outputLayerSize + "):");
        for (int i = 0; i < weights.length; i++) {
            System.out.println("Weights matrix #" + (i + 1) + ": "
                    + weights[i].getHeight() + "x" + weights[i].getWidth());
        }
    }

    public Matrix predict(double[] input) throws Exception {
        if (input.length != inputLayerSize) {
            throw new Exception("Input layer has wrong size: " + input.length + ". Must has: " + inputLayerSize);
        }
        double[][] processedInput = new double[input.length][1];
        for (int i = 0; i < input.length; i++) {
            processedInput[i][0] = input[i];
        }
        return predict(new Matrix(processedInput));
    }

    public Matrix predict(Matrix input) throws Exception {
        System.out.println("Input: " + input.getHeight() + " x " + input.getWidth());
        input.addCol();
        Matrix lastLayer = input;
        for (int i = 0; i < weights.length; i++) {
            System.out.println("Layer #" + i + ": " + lastLayer.getHeight() + "x" + lastLayer.getWidth() + "..." + weights[i].getHeight() + "x" + weights[i].getWidth());
            lastLayer = sigmoid(lastLayer.multiply(weights[i]));
            if (i != weights.length - 1) {
                lastLayer.addCol();
            }
        }
        if (lastLayer.getWidth() != outputLayerSize) {
            System.err.println("Wrong output layer size. " + lastLayer.getWidth() + " instead " + outputLayerSize);
        }
        Matrix output = new Matrix(1, lastLayer.getHeight());
        for (int i = 0; i < lastLayer.getHeight(); i++) {
            int maxIndex = 0;
            for (int j = 0; j < lastLayer.getWidth(); j++) {
                if (lastLayer.get(i, j).value() > lastLayer.get(i, maxIndex).value()) {
                    maxIndex = j;
                }
            }
            output.set(i, 0, new NumericExpression(maxIndex+1));
        }
//        return lastLayer;
        return output;
    }

    private static double sigmoid(double z) {
        return 1d / (1 + Math.exp(-z));
    }

    private static Matrix sigmoid(Matrix src) {
        Matrix dest = new Matrix(src.getWidth(), src.getHeight());
        for (int i = 0; i < dest.getHeight(); i++) {
            for (int j = 0; j < dest.getWidth(); j++) {
                dest.set(i, j, new NumericExpression(sigmoid(src.get(i, j).value())));
            }
        }
        return dest;
    }

    public void setWeights(Matrix... weights) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].getHeight(); j++) {
                for (int k = 0; k < weights[i].getWidth(); k++) {
                    this.weights[i].set(j, k, weights[i].get(j, k));
                }
            }
        }
    }
}
