package gui;

import core.exceptions.IllegalComponentException;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class PageEdge extends Group {

    private PagePoint firstEndpoint;
    private PagePoint secondEndpoint;

    @Deprecated // bad practice
    private STBEdge edge;

    private Line background;

    PageEdge(PagePoint firstEndpoint, PagePoint secondEndpoint) {
        this.firstEndpoint = firstEndpoint;
        this.secondEndpoint = secondEndpoint;

        backgroundInit();

        this.getChildren().add(background);
    }

    private void backgroundInit() {
        background = new Line();
        background.startXProperty().bind(firstEndpoint.getTerminal().getLocation().getXProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        background.startYProperty().bind(firstEndpoint.getTerminal().getLocation().getYProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        background.endXProperty().bind(secondEndpoint.getTerminal().getLocation().getXProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        background.endYProperty().bind(secondEndpoint.getTerminal().getLocation().getYProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        TODO: background.getStyleClass().add(StylesheetsConstants.EDGE_BACKGROUND);
    }

    @Deprecated // bad practice
    public void setEdge(STBEdge edge) {
        this.edge = edge;
    }

    @Deprecated // bad practice
    public STBEdge getEdge() {
        return edge;
    }

    @Deprecated // bad practice
    public STBEdge genEdge(STBTerminal firstEndpoint, STBTerminal secondEndpoint) {
        try {
            this.edge = new EuclideanEdge((EuclideanTerminal) firstEndpoint, (EuclideanTerminal) secondEndpoint);
        } catch (IllegalComponentException e) {
            // Never be here
        }
        return edge;
    }
}
