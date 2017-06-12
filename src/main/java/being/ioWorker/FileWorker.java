package being.ioWorker;

import being.mathematics.MathUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum FileWorker {
    INSTANCE;

    private static final String LAUNCH_DATE_TIME_FOR_FOLDER = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("MM.dd - HH.mm.ss"));
    private static final String LAUNCH_DATE_TIME_FOR_FILE = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("MM-dd  HH-mm-ss"));
    private static final String PATH_SEPARATOR = "\\";

    public int[] getFileDimension(String fileName) {
        int[] dims = new int[2];
        // dim[0] = width
        // dim[1] = height
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            br = new BufferedReader(new FileReader(fileName));
            dims[1] = 0;
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
                int numbersAmount = sCurrentLine.split(",").length;
                if (dims[0] == 0) {
                    dims[0] = numbersAmount;
                }
                if (numbersAmount > 0) {
                    dims[1]++;
                }
            }
            return dims;
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
        return null;
    }

    public double[][] read(String fileName) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            br = new BufferedReader(new FileReader(fileName));
            int[] dims = getFileDimension(fileName);
            double[][] matrix = new double[dims[0]][dims[1]];
            int i = 0;
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
                String[] numbers = sCurrentLine.split(",");
                int j = 0;
                for (String s : numbers) {
                    if (s.trim().length() > 0) {
                        Double d = Double.valueOf(s);
                        matrix[j][i] = d;
                        j++;
                    }
                }
                i++;
            }
            return matrix;
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
        return null;
    }

    public void write(String fullFileName, String text) {
        write(fullFileName, text, WritingMode.APPEND);
    }

    public void write(String fullFileName, String text, WritingMode mode) {
        boolean appendInEof = true;
        fullFileName = processFileName(fullFileName, mode);
        if (mode == WritingMode.REPLACE) {
            appendInEof = false;
        }
        new File(fullFileName).getParentFile().mkdirs();
        try {
            new File(fullFileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fw = new FileWriter(fullFileName, appendInEof);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String[] lines = text.split("[\r\n]");
            for (String line : lines) {
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processFileName(String fullFileName, WritingMode mode) {
        File absoluteFile = new File(fullFileName).getAbsoluteFile();
        fullFileName = absoluteFile.getAbsolutePath();
        if (absoluteFile.exists()) {
            boolean nodeExists;
            int n;
            switch (mode) {
                case CREATE_NEW:
                    nodeExists = true;
                    for (n = 0; n < 1000 && nodeExists; n++) {
                        nodeExists = new File(fullFileName + "_" + n).exists();
                    }
                    if (nodeExists) {
                        try {
                            throw new Exception("Too many files version");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    fullFileName = fullFileName + "_" + n;
                    break;
                case THROW_ERROR:
                    try {
                        throw new Exception("File '" + fullFileName + "' is already exists");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CREATE_NEW_FOLDER:
                    System.out.println("---");
                    nodeExists = true;
                    File file = new File(fullFileName);
                    System.out.println(absoluteFile.getPath());
                    System.out.println(absoluteFile.getParentFile());
                    String folder = absoluteFile.getParentFile().toString();
                    String fileName = file.getName();
                    System.out.println("folder = " + folder);
                    for (n = 0; n < 1000 && nodeExists; n++) {
                        System.out.println("fileName #" + n);
                        nodeExists = new File(folder + "_" + n + PATH_SEPARATOR + fileName).exists();
                    }
                    if (nodeExists) {
                        try {
                            throw new Exception("Too many files version");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    fullFileName = folder + "_" + n + PATH_SEPARATOR + fileName;
                    break;
            }
        }
        switch (mode) {
            case WITH_DATE:
                String parent = absoluteFile.getParent();
                fullFileName = parent + " " + LAUNCH_DATE_TIME_FOR_FOLDER + PATH_SEPARATOR + absoluteFile.getName();
                break;
        }
        return fullFileName;
    }

    public void write(String fileName, double values[], int digitsAfterComa) {
        write(fileName, values, digitsAfterComa, WritingMode.APPEND);
    }

    public void write(String fileName, double values[], int digitsAfterComa, WritingMode mode) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length - 1; i++) {
            builder.append(MathUtils.round(values[i], digitsAfterComa)).append(", ");
        }
//        for (double value : values) {
//            builder.append(MathUtils.round(value, digitsAfterComa)).append(", ");
//        }
        builder.append(MathUtils.round(values[values.length - 1], digitsAfterComa));
        builder.append("\r\n");
        write(fileName, builder.toString(), mode);
    }

    public void write(String fileName, double values[]) {
        write(fileName, values, 100);
    }

    public void write(String fileName, double values[][]) {
        StringBuilder builder = new StringBuilder();
        for (double[] value : values) {
            for (int j = 0; j < value.length - 1; j++) {
                builder.append(value[j]).append(", ");
            }
            builder.append(value[value.length - 1]);
            builder.append("\r\n");
        }
        write(fileName, builder.toString());
    }

    public static class RegisteredFiles {
        public static final String TRAINING_INPUT_DATA_FILE_NAME = "out\\train_data\\training_input_data.csv";
        public static final String TRAINING_OUTPUT_DATA_FILE_NAME = "out\\train_data\\training_output_data.csv";
    }

    public enum WritingMode {
        CREATE_NEW,
        APPEND,
        REPLACE,
        THROW_ERROR,
        CREATE_NEW_FOLDER,
        WITH_DATE;
    }
}
