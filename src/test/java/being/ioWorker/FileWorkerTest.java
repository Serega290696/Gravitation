package being.ioWorker;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Serega on 05.06.2017.
 */
public class FileWorkerTest {
    private final String fullFileName = "out\\test_output\\test_file";
    private final String text = "random text here";

    @Test
    public void testWrite() throws Exception {
        FileWorker.INSTANCE.write(fullFileName, text, FileWorker.WritingMode.WITH_DATE);
        FileWorker.INSTANCE.write(fullFileName, text, FileWorker.WritingMode.WITH_DATE);
        Thread.sleep(2000);
        FileWorker.INSTANCE.write(fullFileName, text, FileWorker.WritingMode.WITH_DATE);
    }
}