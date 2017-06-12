package being.neural_network;

import being.mathlab.expressions.Expression;
import being.mathlab.expressions.parser.ExpressionParser;
import being.mathlab.matrix.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public enum MatrixReaderFromFile {
    INSTANCE;

    public Matrix readMatrix(String fileName, int w, int h) {
        Matrix m = null;

//        File yFile = new File("D:\\1-Drive\\2-Studies\\VIII\\MathLab\\src\\main\\resources\\nn\\sample\\y.txt");
//        System.out.println(yFile.list()[0]);
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(fileName));
            Expression[][] exps = new Expression[h][w];
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.trim().split(" ");
                for (int j = 0; j < split.length; j++) {
                    String s = split[j].trim();
                    if (!"".equals(s)) {
//                        System.out.println("s = " + s);
                        exps[i][j] = ExpressionParser.parse(s);
                    }
                }
                i++;
            }
            m = new Matrix(exps);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return m;
    }
}
