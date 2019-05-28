package dai;

import core.exceptions.IllegalComponentException;
import core.implementations.GraphPage;
import core.implementations.ResultGraphPage;
import core.implementations.algorithms.other.KruskallAlgorithm;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBLocation;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import dai.entities.ORLibraryResult;
import dai.entities.ORLibraryTask;
import utils.IdUtils;
import utils.iou.FileUtils;
import utils.iou.ORLibraryUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public final class ORLibraryAccess {

    private ORLibraryAccess() {}

    public static Map<Integer, GraphPage> getTasks(File taskFile) throws FileNotFoundException {
        List<ORLibraryTask> tasks = ORLibraryUtils.readTasks(taskFile);
        Map<Integer, GraphPage> pages = new HashMap<>();
        tasks.forEach(task -> {
            try {
                GraphPage page = new GraphPage(new EuclideanGraph());
                page.nameProperty().set("task#" + task.id);
                task.getPoints().forEach(point -> page.getGraph().addVertex(new EuclideanTerminal(point, IdUtils.getTerminalId(page.getGraph()))));
                pages.put(task.id, page);
            } catch (IllegalComponentException e) {
                e.printStackTrace();
            }
        });
        return pages;
    }

    public static Map<Integer, ResultGraphPage> getResults(File taskFile) throws FileNotFoundException {
        Map<Integer, GraphPage> tasks = getTasks(taskFile);
        Map<Integer, ResultGraphPage> pages = new HashMap<>();
        String[] path = taskFile.getPath().split("/");
        String[] name = path[path.length - 1].split("\\.");
        name[0] += "opt";
        path[path.length - 1] = String.join(".", name);
        String resultFileName = String.join("/", path);
        List<ORLibraryResult> results = ORLibraryUtils.readResults(new File(resultFileName));
        results.forEach(result -> {
            try {
                GraphPage taskPage = tasks.get(result.id);
                result.getPoints().forEach(point -> {
                    EuclideanTerminal steinerPoint = new EuclideanTerminal(point, IdUtils.getTerminalId(taskPage.getGraph()));
                    steinerPoint.typeProperty().setValue(STBTerminalType.STEINER_TERMINAL);
                    taskPage.getGraph().addVertex(steinerPoint);
                });
                KruskallAlgorithm algorithm = new KruskallAlgorithm<>((EuclideanGraph) taskPage.getGraph());
                algorithm.run();
                ResultGraphPage page = new ResultGraphPage(algorithm.getResult());
                page.nameProperty().set("result#" + result.id);
                page.putProperty("algorithm", "Exact Algorithm (ORLibrary)");
                page.putProperty("mst weight", "" + result.weightMST);
                page.putProperty("smt weight", "" + result.weightSMT);
                pages.put(result.id, page);
            } catch (IllegalComponentException e) {
                e.printStackTrace();
            }
        });
        return pages;
    }

    private static int case_id = 0;
    private static int point_id = 0;
    private static int result_id = 0;
    public static void getHehs(File hehFile) throws FileNotFoundException {
        hehResultFile = new File(hehFile.getParent() + "/heh_result");
        Map<ORLibraryTask, ORLibraryResult> pre_hehs = ORLibraryUtils.readHehs(hehFile);

        List<Map.Entry<ORLibraryTask, ORLibraryResult>> list = new ArrayList(pre_hehs.entrySet());
        list.sort(Comparator.comparingInt(o -> o.getKey().getPoints().size()));

        Map<ORLibraryTask, ORLibraryResult> hehs = new LinkedHashMap<>();
        for (Map.Entry<ORLibraryTask, ORLibraryResult> entry : list) {
            hehs.put(entry.getKey(), entry.getValue());
        }

        hehs.forEach((task, result) -> {
            case_id++;
            // cases:
            String _case = "(" + case_id + ",\t" + result.weightMST + ",\t" + result.weightSMT + ",\t" + task.getPoints().size() + "),";
            writeHeh(_case);
            task.getPoints().forEach(point -> {
                String _case_point = "";
                point_id++;
                // case_points:
                _case_point += "\t\t\t\t\t\t(" + case_id + ",\t" + point_id + "',";
                // points:
                _case_point += "\t\t(" + point_id + ",\t" + ((STBLocation) point).xProperty().get() + ",\t" + ((STBLocation) point).yProperty().get() + "),";
                writeHeh(_case_point);
            });
            result_id++;
            // cases:
            String _result = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t(" + result_id + ",\t" + case_id + ",\t" + "1" + ",\t" + result.weightMST + ",\t" + "null" +  "),";
            writeHeh(_result);
            result.getPoints().forEach(point -> {
                String _steiner_point = "";
                point_id++;
                // points:
                _steiner_point += "\t\t\t\t\t\t\t\t\t(" + point_id + ",\t" + point.xProperty().get() + ",\t" + point.yProperty().get() + "),";
                // steiner_points:
                _steiner_point += "\t\t(" + result_id + ",\t" + point_id + "),";
                writeHeh(_steiner_point);
            });
        });
//        writeHeh("Ends with: " + "case_id = " + case_id  + "\tpoint_id = " + point_id + "\tresult_id = " + result_id);
    }

    private static File hehResultFile;
    private static void writeHeh(String line) {
        try {
            FileUtils.append(hehResultFile, line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
