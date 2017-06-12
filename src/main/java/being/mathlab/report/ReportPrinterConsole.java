package being.mathlab.report;

import being.mathlab.matrix.Matrix;
import being.mathlab.utils.MathUtil;

public class ReportPrinterConsole<R> implements ReportPrinter<R> {

    public void print(Report<R> report) {
        System.out.println();
        System.out.println(" ============== Report ============== ");
        if (report == null) {
            System.out.println(" || Empty report.");
        } else {
            if (report.isSuccessful()) {
                if (report.getResult() != null) {
                    if (report.getResult() instanceof Matrix) {
                        System.out.println(" || Result:");
                        Matrix result = (Matrix) report.getResult();
                        for (int i = 0; i < result.getHeight(); i++) {
                            for (int j = 0; j < result.getWidth(); j++) {
                                System.out.print(" || X[" + (1 + i) + "]");
                                if (result.getWidth() > 1) {
                                    System.out.println("[" + (j + 1) + "]");

                                }
                                System.out.println(" = " + result.get(i, j));
                            }
                        }
                    } else {
                        Number result = (Number) report.getResult();
                        System.out.println(" || Result: " + MathUtil.round(result, 4) + ".");
                    }
                } else {
                    System.out.println(" || Result is empty.");
                }
                System.out.println(" || Spent " + report.getElapsedTimeUnit().toSeconds(report.getSpentTime()) + " seconds.");
            } else {
                System.out.println(" || Computing is failed");
            }
        }
        System.out.println(" ==================================== ");
    }

}
