package ai.execution;

import utils.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonExecutor {

    private PythonExecutor() {
    }

    public static String executePythonScript(
            String scriptPath)
            throws Exception {
        String line;

        ProcessBuilder builder =
                new ProcessBuilder(
                        "python",
                        scriptPath);

        Process process =
                builder.start();

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getInputStream()));

        BufferedReader errorReader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getErrorStream()));

        while ((line = errorReader.readLine()) != null) {

            LoggerUtil.LOGGER.error(
                    "[AI-EVAL] PYTHON ERROR : {}",
                    line);
        }

        StringBuilder output =
                new StringBuilder();



        while ((line = reader.readLine()) != null) {

            output.append(line);
        }

        process.waitFor();

        return output.toString();
    }

    public static String executePythonScript(
            String scriptPath,
            String question,
            String answer,
            String context,
            String metric)
            throws Exception {

        ProcessBuilder builder =
                new ProcessBuilder(
                        "python",
                        scriptPath,
                        question,
                        answer,
                        context,
                        metric);

        Process process =
                builder.start();

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getInputStream()));

        BufferedReader errorReader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getErrorStream()));

        StringBuilder output =
                new StringBuilder();

        String line;

        while ((line = errorReader.readLine()) != null) {

            LoggerUtil.LOGGER.error(
                    "[AI-EVAL] PYTHON ERROR : {}",
                    line);
        }

        while ((line = reader.readLine()) != null) {

            output.append(line);
        }

        process.waitFor();

        return output.toString();
    }

    public static String executePythonScript(
            String scriptPath,
            String... args)
            throws Exception {

        ProcessBuilder builder =
                new ProcessBuilder();

        java.util.List<String> command =
                new java.util.ArrayList<>();

        command.add("python");
        command.add(scriptPath);

        for (String arg : args) {
            command.add(arg);
        }

        builder.command(command);

        Process process =
                builder.start();

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getInputStream()));

        BufferedReader errorReader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getErrorStream()));

        StringBuilder output =
                new StringBuilder();

        String line;

        while ((line = errorReader.readLine()) != null) {

            LoggerUtil.LOGGER.error(
                    "[AI-EVAL] PYTHON ERROR : {}",
                    line);
        }

        while ((line = reader.readLine()) != null) {

            output.append(line);
        }

        process.waitFor();

        return output.toString();
    }
}