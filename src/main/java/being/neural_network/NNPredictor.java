package being.neural_network;

import being.ioWorker.FileWorker;
import being.mathlab.expressions.types.NumericExpression;
import being.mathlab.matrix.Matrix;

public class NNPredictor {
    //    static Scanner scanner = new Scanner(System.in);
//    static boolean pause = false;
//    private static Matrix x;
    private static Matrix w2;
    private static Matrix w1;
    private static final String filesStoragePath = "out\\trajectory\\";
    //    private static final String filesStoragePath = "D:\\1-Drive\\1-Programming\\1-Projects\\Part5\\Gravity\\out\\";
//    private static final String defaultInputFileName = "x.txt";
//    private static final String defaultTargetOutputFileName = "y.txt";
//    private static final String defaultWeightsMatrix1FileName = "w1.txt";
//    private static final String defaultWeightsMatrix2FileName = "w2.txt";
//    private String inputFileName = defaultInputFileName;
//    private String targetOutputFileName = defaultTargetOutputFileName;
//    private String weightsMatrix1FileName = defaultWeightsMatrix1FileName;
//    private String weightsMatrix2FileName = defaultWeightsMatrix2FileName;

    public NNPredictor(String weightsMatrix1FileName, String weightsMatrix2FileName) {
        double[][] w1Array = FileWorker.INSTANCE.read(
                filesStoragePath + weightsMatrix1FileName);
        w1 = new Matrix(w1Array);
        double[][] w2Array = FileWorker.INSTANCE.read(
                filesStoragePath + weightsMatrix2FileName);
        w2 = new Matrix(w2Array);
    }

//    public static void init() {
//        Matrix y = MatrixReaderFromFile.INSTANCE.readMatrix(
//                filesStoragePath + defaultTargetOutputFileName, 1, 5000);
////        System.out.println(y);
////        System.out.println("^ y");
////        if (pause) {
////            scanner.nextLine();
////        }
//        w1 = MatrixReaderFromFile.INSTANCE.readMatrix(
//                filesStoragePath + defaultWeightsMatrix1FileName,
//                401, 25);
////        System.out.println(w1);
////        System.out.println("^ w1");
////        if (pause) {
////            scanner.nextLine();
////        }
//        w2 = MatrixReaderFromFile.INSTANCE.readMatrix(
//                filesStoragePath + defaultWeightsMatrix2FileName,
//                26, 10);
////        System.out.println(w2);
////        System.out.println("^ w2");
////        if (pause) {
////            scanner.nextLine();
////        }
//        x = MatrixReaderFromFile.INSTANCE.readMatrix(
//                filesStoragePath + defaultInputFileName,
//                400, 5000);
////        System.out.println(x);
////        System.out.println("^ X");
//////        if (pause) {
//////            scanner.nextLine();
//////        }
//////        x.addCol();
//////        System.out.println("==============================");
//////        System.out.println("========== 1 ===============");
//////        System.out.println("==============================");
//////        Matrix m1 = x.multiply(w1.transpose());
//////        System.out.println(m1);
//////        if (pause) {
//////            scanner.nextLine();
//////        }
//////        System.out.println("==============================");
//////        System.out.println("========== 2 ===============");
//////        System.out.println("==============================");
//////        Matrix s1 = sigmoid(m1);
//////        System.out.println("CELL");
//////        System.out.println(s1.getWidth());
//////        System.out.println(s1.getHeight());
//////        s1.addCol();
//////        System.out.println(s1.getWidth());
//////        System.out.println(s1.getHeight());
//////        System.out.println(s1);
//////        if (pause) {
//////            scanner.nextLine();
//////        }
//////        System.out.println("==============================");
//////        System.out.println("========== 3 ===============");
//////        System.out.println("==============================");
//////        Matrix m2 = s1.multiply(w2.transpose());
//////        System.out.println(m2);
//////        if (pause) {
//////            scanner.nextLine();
//////        }
//////        System.out.println("==============================");
//////        System.out.println("========== 4 ===============");
//////        System.out.println("==============================");
//////        Matrix s2 = sigmoid(m2);
//////        System.out.println(s2);
//////        if (pause) {
//////            scanner.nextLine();
//////        }
//////        System.out.println("==============================");
//////        System.out.println("========== 5 ===============");
//////        System.out.println("==============================");
//////        Matrix y1 = new Matrix(1, x.getHeight());
//////        for (int i = 0; i < s2.getHeight(); i++) {
//////            int indexMax = 0;
//////            for (int j = 0; j < s2.getWidth(); j++) {
//////                if (s2.get(i, indexMax).value() < s2.get(i, j).value()) {
//////                    indexMax = j;
//////                }
//////            }
//////            y1.set(i, 0, new NumericExpression(indexMax + 1));
//////        }
//////        System.out.println(y1);
//////        if (pause) {
//////            scanner.nextLine();
//////        }
//////        System.out.println("==============================");
//////        System.out.println("========== FINISH ==========");
//////        System.out.println("==============================");
//////        for (int i = 0; i < y.getHeight(); i++) {
//////            System.out.println(y1.get(i, 0).value() == y.get(i, 0).value());
//////        }
//    }

    public static int predict(double[] bitMap) {
        double[][] bitMapMatrix = new double[1][400];
        for (int i = 0; i < 400; i++) {
//            for (int j = 0; j < bitMapMatrix.length; j++) {
            bitMapMatrix[0][i] = bitMap[i];
//            }
        }
        Matrix x = new Matrix(bitMapMatrix);
        x.addCol();
        Matrix m1 = x.multiply(w1.transpose());
        Matrix s1 = sigmoid(m1);
        s1.addCol();
        Matrix m2 = s1.multiply(w2.transpose());
        Matrix s2 = sigmoid(m2);
        Matrix y1 = new Matrix(1, x.getHeight());
        for (int i = 0; i < s2.getHeight(); i++) {
            int indexMax = 0;
            for (int j = 0; j < s2.getWidth(); j++) {
//                System.out.println(i + " : " + j + ". " + s2.get(i, j).value());
                if (s2.get(i, indexMax).value() < s2.get(i, j).value()) {
                    indexMax = j;
                }
            }
            y1.set(i, 0, new NumericExpression(indexMax + 1));
        }
        System.out.println(y1);
//        if (pause) {
//            scanner.nextLine();
//        }
        return (int) y1.get(0, 0).value();
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

}
