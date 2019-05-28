package gui;

import appi.ci.interfaces.ProjectPointView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PagePoint extends StackPane implements ProjectPointView {

    // TODO: replace next 3 lines into configuration
    public static final double REGULAR_POINT_RADIUS = 6.;
    public static final double STEINER_POINT_RADIUS = 4.;
    public static final double HALO_BORDER_OFFSET = 4.;
    public static final double HALO_SIZE = REGULAR_POINT_RADIUS * 2 + HALO_BORDER_OFFSET * 2;

    private Circle background;
    private Rectangle halo;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isFirstEndpoint = new SimpleBooleanProperty(false);
    private BooleanProperty isSecondEndpoint = new SimpleBooleanProperty(false);

    PagePoint(double x, double y) {
        this.layoutXProperty().set(x);
        this.layoutYProperty().set(y);

        this.backgroundInit();
        this.haloInit();

        this.setOnMouseClicked(event -> this.isSelected.set(!this.isSelected.get()));

        this.getChildren().addAll(this.halo, this.background);
    }

    @Override
    public void setTerminalType(PseudoClass type) {
        if (type.equals(StylesheetsConstants.PSEUDO_CLASS_SIMPLE_TERMINAL)) {
            this.background.setRadius(REGULAR_POINT_RADIUS);
            this.background.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_STEINER_TERMINAL, false);
            this.background.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SIMPLE_TERMINAL, true);
        }
        if (type.equals(StylesheetsConstants.PSEUDO_CLASS_STEINER_TERMINAL)) {
            this.background.setRadius(STEINER_POINT_RADIUS);
            this.background.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SIMPLE_TERMINAL, false);
            this.background.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_STEINER_TERMINAL, true);
        }
    }

    private void backgroundInit() {
        this.background = new Circle(REGULAR_POINT_RADIUS);
        this.background.getStyleClass().add(StylesheetsConstants.TERMINAL_BACKGROUND);
        this.setTerminalType(StylesheetsConstants.PSEUDO_CLASS_SIMPLE_TERMINAL);
        this.isSelected.addListener((observable, oldValue, newValue) -> {
            this.halo.visibleProperty().set(newValue);
            this.background.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SELECTED, newValue);
        });
    }

    private void haloInit() {
        this.halo = new Rectangle(HALO_SIZE, HALO_SIZE);
        this.halo.getStyleClass().add(StylesheetsConstants.HALO);
        this.halo.visibleProperty().set(false);
        this.isFirstEndpoint.addListener((observable, oldValue, newValue) -> {
            this.halo.visibleProperty().set(newValue);
            this.halo.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_FIRSTENDPOINT, newValue);
        });
        this.isSecondEndpoint.addListener((observable, oldValue, newValue) -> {
            this.halo.visibleProperty().set(newValue);
            this.halo.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SECONDENDPOINT, newValue);
        });
    }

    @Override
    public BooleanProperty isFirstEndpoint() {
        return this.isFirstEndpoint;
    }

    @Override
    public BooleanProperty isSecondEndpoint() {
        return this.isSecondEndpoint;
    }

    @Override
    public BooleanProperty isSelectedProperty() {
        return this.isSelected;
    }

    @Override
    public DoubleProperty xPropertyProperty() {
        return this.layoutXProperty();
    }

    @Override
    public DoubleProperty yPropertyProperty() {
        return this.layoutYProperty();
    }

    @Override
    public void select() {
        this.isSelected.set(true);
    }

    @Override
    public void unselect() {
        this.isSelected.set(false);
        this.isFirstEndpoint.set(false);
        this.isSecondEndpoint.set(false);
    }

    @Override
    public void delete() {
        this.unselect();
    }
}
