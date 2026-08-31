package reporting;

import java.io.IOException;
import java.nio.file.*;

public class HistoryManager {

    public static void copyHistory() {

        try {

            Path source =
                    Paths.get(
                            "reports/allure-report/history");

            Path target =
                    Paths.get(
                            "reports/allure-results/history");

            if (Files.exists(source)) {

                Files.walk(source)
                        .forEach(path -> {

                            try {

                                Path destination =
                                        target.resolve(
                                                source.relativize(path));

                                if (Files.isDirectory(path)) {

                                    Files.createDirectories(
                                            destination);

                                } else {

                                    Files.copy(
                                            path,
                                            destination,
                                            StandardCopyOption.REPLACE_EXISTING);
                                }

                            } catch (IOException e) {

                                throw new RuntimeException(e);
                            }
                        });
            }

        } catch (Exception e) {

            System.out.println(
                    "No previous history found");
        }
    }
}