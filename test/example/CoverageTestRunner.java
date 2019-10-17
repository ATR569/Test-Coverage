package example;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import main.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

/**
 *
 * @author Adson Macêdo
 * @author Thairam Michel
 */
public class CoverageTestRunner {

    private static Map<String, Set<String>> coverageMap;
    private static final String PATH = System.getProperty("user.dir");
    private static final String LOGFILE = "log.txt";

    public static void runTests() {
        JUnitCore junit = new JUnitCore();
        junit.run(Request.classes(TriangleTest.class));
    }

    public static void calcCoverage() throws IOException {
        System.out.println("COBERTURA DO CODIGO");

        for (String sourceFilePath : coverageMap.keySet()) {
            Set<String> s = coverageMap.get(sourceFilePath);

            double coverage = ProcessLogs.getCoverage(LOGFILE, s);

            System.out.println("Arquivo: " + sourceFilePath);
            System.out.printf("Cobertura: %.2f %%\n", coverage);
        }
    }

    public static void processFiles() {
        System.out.println(PATH);
        coverageMap = CoverageTransform.parseFiles(PATH + "\\src\\source", PATH + "\\src\\target");

        System.out.println();
        System.out.println("========================================================================================");
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            processFiles();
            Utils.resetLog(LOGFILE);
            runTests();
            calcCoverage();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
