package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Adson Macêdo
 * @author Thairam Michel
 */
public class ProcessLogs {

    public static double getCoverage(String filePath, Set<String> coverable) throws IOException {
        Set<String> covered = new HashSet<>();
        covered.addAll(Arrays.asList(Utils.getFileAsString(filePath).split("\n")));

        int coverableCommands = coverable.size();
        int coveredCommands = 0;

        if (coverableCommands == 0) {
            return 100;
        } else {
            Iterator baseLog = coverable.iterator();

            while (baseLog.hasNext()) {
                String source = baseLog.next().toString();

                if (covered.contains(source)) {
                    coveredCommands++;
                }
            }

            return 100.0*coveredCommands/coverableCommands;
        }
    }
}
