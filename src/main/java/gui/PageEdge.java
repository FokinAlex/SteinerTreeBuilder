package gui;

import core.exceptions.IllegalComponentException;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class PageEdge extends Group {

    private static final double SELECTOR_SIZE = 8.;
    private static final double SELECTOR_HALO_OFFSET = 4.;
    public static final double SELECTOR_HALO_SIZE = SELECTOR_SIZE + SELECTOR_HALO_OFFSET * 2;

    private final PagePoint firstEndpoint;
    private final PagePoint secondEndpoint;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private DoubleProperty lengthProperty = new SimpleDoubleProperty();

    @Deprecated // bad practice
    private STBEdge edge;

    private Line background;
    private Pane selectorGroup;
    private Rectangle selector;
    private Rectangle selectorHalo;

    PageEdge(PagePoint firstEndpoint, PagePoint secondEndpoint) {
        this.firstEndpoint = firstEndpoint;
        this.secondEndpoint = secondEndpoint;

        this.setOnMouseClicked(event -> {
            ((PagePane) this.getParent()).selectEdge(this.isSelected.get() ? null : this);
            this.firstEndpoint.setIsFirstEndpoint(true);
            this.secondEndpoint.setIsSecondEndpoint(true);
        });

        this.backgroundInit();
        this.selectorInit();

        this.getChildren().addAll(this.background, this.selectorGroup);
    }

    private void backgroundInit() {
        this.background = new Line();
        this.background.startXProperty().bind(this.firstEndpoint.getTerminal().getLocation().getXProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.startYProperty().bind(this.firstEndpoint.getTerminal().getLocation().getYProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.endXProperty().bind(this.secondEndpoint.getTerminal().getLocation().getXProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.endYProperty().bind(this.secondEndpoint.getTerminal().getLocation().getYProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.getStyleClass().add(StylesheetsConstants.EDGE_BACKGROUND);
    }

    private void selectorInit() {
        this.selectorGroup = new Pane();
        this.selectorGroup.layoutXProperty().bind(this.background.startXProperty().add(this.background.endXProperty()).subtract(SELECTOR_SIZE).divide(2));
        this.selectorGroup.layoutYProperty().bind(this.background.startYProperty().add(this.background.endYProperty()).subtract(SELECTOR_SIZE).divide(2));

        this.selector = new Rectangle(SELECTOR_SIZE, SELECTOR_SIZE);
        this.selector.getStyleClass().add(StylesheetsConstants.EDGE_SELECTOR);
        this.isSelected.addListener((observable, oldValue, newValue) -> this.selector.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SELECTED, newValue));

        this.selectorHalo = new Rectangle(SELECTOR_HALO_SIZE, SELECTOR_HALO_SIZE);
        this.selectorHalo.setLayoutX(-SELECTOR_HALO_OFFSET);
        this.selectorHalo.setLayoutY(-SELECTOR_HALO_OFFSET);
        this.selectorHalo.getStyleClass().add(StylesheetsConstants.HALO);
        this.selectorHalo.visibleProperty().bind(this.isSelected);

        this.selectorGroup.getChildren().addAll(this.selector, this.selectorHalo);
    }

    public PagePoint getFirstEndpoint() {
        return firstEndpoint;
    }

    public PagePoint getSecondEndpoint() {
        return secondEndpoint;
    }

    public void delete() {
        this.background.startXProperty().unbind();
        this.background.startYProperty().unbind();
        this.background.endXProperty().unbind();
        this.background.endYProperty().unbind();
        this.selectorGroup.layoutXProperty().unbind();
        this.selectorGroup.layoutYProperty().unbind();
        this.edge = null;
        this.unselect();
    }

    @Deprecated // bad practice
    public void setEdge(STBEdge edge) {
        this.edge = edge;
        this.lengthProperty.bind(edge.getLengthProperty());
    }

    @Deprecated // bad practice
    public STBEdge getEdge() {
        return edge;
    }

    @Deprecated // bad practice
    public STBEdge genEdge(STBTerminal firstEndpoint, STBTerminal secondEndpoint) {
        this.setEdge(new EuclideanEdge((EuclideanTerminal) firstEndpoint, (EuclideanTerminal) secondEndpoint));
        return edge;
    }

    public DoubleProperty getLengthProperty() {
        return lengthProperty;
    }

    public void select() {
        this.isSelected.set(true);
        this.firstEndpoint.select();
        this.firstEndpoint.setIsFirstEndpoint(true);
        this.secondEndpoint.select();
        this.secondEndpoint.setIsSecondEndpoint(true);
    }

    public void unselect() {
        this.isSelected.set(false);
        this.firstEndpoint.unselect();
        this.secondEndpoint.unselect();
    }
}
