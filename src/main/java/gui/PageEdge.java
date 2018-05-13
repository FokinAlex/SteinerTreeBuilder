package gui;

import appi.ci.interfaces.ProjectEdgeView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class PageEdge extends Group implements ProjectEdgeView {

    // TODO: replace next 3 lines into configuration
    private static final double SELECTOR_SIZE = 8.;
    private static final double SELECTOR_HALO_OFFSET = 4.;
    private static final double SELECTOR_HALO_SIZE = SELECTOR_SIZE + SELECTOR_HALO_OFFSET * 2;

    private final PagePoint firstEndpoint;
    private final PagePoint secondEndpoint;

    private Line background;
    private Pane selectorGroup;
    private Rectangle selector;
    private Rectangle selectorHalo;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    PageEdge(PagePoint firstEndpoint, PagePoint secondEndpoint) {
        this.firstEndpoint = firstEndpoint;
        this.secondEndpoint = secondEndpoint;

        this.backgroundInit();
        this.selectorInit();

        this.setOnMouseClicked(event -> this.isSelected.set(!this.isSelected.get()));

        this.getChildren().addAll(this.background, this.selectorGroup);
    }

    private void backgroundInit() {
        this.background = new Line();
        this.background.startXProperty().bind(this.firstEndpoint.xPropertyProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.startYProperty().bind(this.firstEndpoint.yPropertyProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.endXProperty().bind(this.secondEndpoint.xPropertyProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
        this.background.endYProperty().bind(this.secondEndpoint.yPropertyProperty().add(PagePoint.REGULAR_POINT_RADIUS + PagePoint.HALO_BORDER_OFFSET));
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

    @Override
    public PagePoint getFirstEndpoint() {
        return this.firstEndpoint;
    }

    @Override
    public PagePoint getSecondEndpoint() {
        return this.secondEndpoint;
    }

    @Override
    public BooleanProperty isSelectedProperty() {
        return this.isSelected;
    }

    @Override
    public void select() {
        this.isSelected.set(true);
    }

    @Override
    public void unselect() {
        this.isSelected.set(false);
    }

    @Override
    public void delete() {
        this.background.startXProperty().unbind();
        this.background.startYProperty().unbind();
        this.background.endXProperty().unbind();
        this.background.endYProperty().unbind();
        this.selectorGroup.layoutXProperty().unbind();
        this.selectorGroup.layoutYProperty().unbind();
        this.unselect();
    }
}
