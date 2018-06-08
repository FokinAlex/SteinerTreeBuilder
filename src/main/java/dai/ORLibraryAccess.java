package dai;

import core.exceptions.IllegalComponentException;
import core.implementations.GraphPage;
import core.implementations.ResultGraphPage;
import core.implementations.algorithms.other.KruskallAlgorithm;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import core.types.STBTerminalType;
import dai.entities.ORLibraryResult;
import dai.entities.ORLibraryTask;
import utils.IdUtils;
import utils.iou.ORLibraryUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
