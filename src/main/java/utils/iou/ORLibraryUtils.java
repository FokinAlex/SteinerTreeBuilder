package utils.iou;

import dai.entities.ORLibraryResult;
import dai.entities.ORLibraryTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class ORLibraryUtils {

    public static final int MULTIPLIER = 1; // 500;

    private ORLibraryUtils() {}

    public static List<ORLibraryTask> readTasks(File file) throws FileNotFoundException {
        List<ORLibraryTask> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        int numberOfTasks = Integer.parseInt(line.replaceAll(" ", ""));
        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            ORLibraryTask task = new ORLibraryTask(taskNumber);
            tasks.add(task);
            line = scanner.nextLine();
            int numberOfPoints = Integer.parseInt(line.replaceAll(" ", ""));
            for (int point = 0; point < numberOfPoints; point++) {
                line = scanner.nextLine();
                String[] values = line.split(" ");
                task.addPoint(Double.parseDouble(values[1]) * MULTIPLIER, Double.parseDouble(values[2]) * MULTIPLIER);
            }
        }
        return tasks;
    }

    public static List<ORLibraryResult> readResults(File file) throws FileNotFoundException {
        List<ORLibraryResult> results = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        int numberOfResults = Integer.parseInt(line);
        for (int resultNumber = 0; resultNumber < numberOfResults; resultNumber++) {
            double weightSMT = Double.parseDouble(scanner.nextLine()) * MULTIPLIER;
            double weightMST = Double.parseDouble(scanner.nextLine()) * MULTIPLIER;
            ORLibraryResult result = new ORLibraryResult(resultNumber, weightMST, weightSMT);
            results.add(result);
            line = scanner.nextLine();
            int numberOfPoints = Integer.parseInt(line);
            for (int point = 0; point < numberOfPoints; point++) {
                line = scanner.nextLine();
                String[] values = line.split(" ");
                result.addPoint(Double.parseDouble(values[0]) * MULTIPLIER, Double.parseDouble(values[1]) * MULTIPLIER);
            }
        }
        return results;
    }

    public static Map<ORLibraryTask, ORLibraryResult> readHehs(File file) throws FileNotFoundException {
        Map<ORLibraryTask, ORLibraryResult> hehs = new HashMap();
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        int numberOfTasks = Integer.parseInt(line.replaceAll(" ", ""));
        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            ORLibraryTask task = new ORLibraryTask(taskNumber);
            line = scanner.nextLine();
            int numberOfPoints = Integer.parseInt(line.replaceAll(" ", ""));
            for (int point = 0; point < numberOfPoints; point++) {
                line = scanner.nextLine();
                String[] values = line.split(" ");
                task.addPoint(Double.parseDouble(values[1]) * MULTIPLIER, Double.parseDouble(values[2]) * MULTIPLIER);
            }
            double weightSMT = Double.parseDouble(scanner.nextLine()) * MULTIPLIER;
            double weightMST = Double.parseDouble(scanner.nextLine()) * MULTIPLIER;
            ORLibraryResult result = new ORLibraryResult(taskNumber, weightMST, weightSMT);
            hehs.put(task, result);
            line = scanner.nextLine();
            numberOfPoints = Integer.parseInt(line);
            for (int point = 0; point < numberOfPoints; point++) {
                line = scanner.nextLine();
                String[] values = line.split(" ");
                result.addPoint(Double.parseDouble(values[0]) * MULTIPLIER, Double.parseDouble(values[1]) * MULTIPLIER);
            }
        }
        return hehs;
    }

}
