package dai;

import appi.ci.implementations.SimpleProject;
import appi.ci.interfaces.Project;
import core.exceptions.IllegalComponentException;
import core.implementations.GraphMultiPageScheme;
import core.implementations.GraphPage;
import core.implementations.ResultGraphPage;
import core.interfaces.STBGraph;
import core.interfaces.STBPage;
import core.interfaces.STBScheme;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ProjectDataAccess {
    SIMPLE {
        @Override
        public JSONObject toJson(Project project) {
            JSONObject root = new JSONObject();
            JSONObject jProject = new JSONObject();
            JSONArray jPages = new JSONArray();

            root.put("project", jProject);
            jProject.put("name", project.nameProperty().get());
            jProject.put("pages", jPages);
            project.getPages().forEach(page -> {
                JSONObject jPage = new JSONObject();
                JSONObject jPagesValues = new JSONObject();
                jPagesValues.put("name", ((STBPage) page).nameProperty().get());
                // TODO: check if not GraphPage
                jPagesValues.put("graph", GraphDataAccess.EUCLIDEAN.toJson((STBGraph) ((STBPage) page).getAllComponents().get(0)));
                if (page instanceof ResultGraphPage) {
                    ((ResultGraphPage) page).getProperties().forEach((name, property) -> jPagesValues.put(name, property));
                    jPage.put("resultpage", jPagesValues);
                } else jPage.put("page", jPagesValues);
                jPages.add(jPage);
            });
            return root;
        }

        @Override
        public Project fromJson(JSONObject json) throws IllegalComponentException {
            STBScheme scheme = new GraphMultiPageScheme();
            STBPage currentPage;

            JSONObject jProject = (JSONObject) json.get("project");
            String projectName = (String) jProject.get("name");
            JSONArray jPages = (JSONArray) jProject.get("pages");
            Iterator<JSONObject> iterator = jPages.iterator();
            while (iterator.hasNext()) {
                JSONObject jPageObject = iterator.next();
                JSONObject jPage = (JSONObject) jPageObject.get("page");
                if (null == jPage) {
                    jPage = (JSONObject) jPageObject.get("resultpage");
                    // TODO: check type
                    STBGraph pageGraph = GraphDataAccess.EUCLIDEAN.fromJson(jPage);
                    currentPage = new ResultGraphPage(pageGraph);
                    Map<String, String> properties = new HashMap<>();
                    jPage.forEach((name, property) -> {
                        if (name instanceof String && property instanceof String)
                            properties.put((String) name, (String) property);
                    });
                    ((ResultGraphPage) currentPage).putProperties(properties);
                } else {
                    // TODO: check type
                    STBGraph pageGraph = GraphDataAccess.EUCLIDEAN.fromJson(jPage);
                    currentPage = new GraphPage(pageGraph);
                }
                String pageName = (String) jPage.get("name");
                currentPage.nameProperty().set(pageName);
                scheme.addPage(currentPage);
            }
            return new SimpleProject((GraphMultiPageScheme) scheme, projectName);
        }
    };

    public abstract JSONObject toJson(Project graph);
    public abstract Project fromJson(JSONObject json) throws IllegalComponentException;
}
