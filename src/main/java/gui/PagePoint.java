package gui;

import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import utils.IdUtils;

public class PagePoint extends StackPane {

    public static final double REGULAR_POINT_RADIUS = 10.;
    public static final double HALO_BORDER_OFFSET = 4.;
    public static final double HALO_SIZE = REGULAR_POINT_RADIUS * 2 + HALO_BORDER_OFFSET * 2;

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isFirstEndpoint = new SimpleBooleanProperty(false);
    private BooleanProperty isSecondEndpoint = new SimpleBooleanProperty(false);

    @Deprecated // Bad practice
    private STBTerminal terminal;

    private Circle background;
    private Rectangle halo;

    PagePoint(double x, double y) {
        this.layoutXProperty().set(x);
        this.layoutYProperty().set(y);

        this.setOnMouseClicked(event -> {
            if (((PagePane) this.getParent()).edgeAdditionModeProperty().get() &&
                    ((PagePane) this.getParent()).selectedTerminalProperty().get() &&
                    (!this.equals(((PagePane) this.getParent()).getSelectedPoint()))) {
                ((PagePane) this.getParent()).addEdge(((PagePane) this.getParent()).getSelectedPoint(), this);
            }
            ((PagePane) this.getParent()).selectPoint(this.isSelected.get() ? null : this);
        });

        this.backgroundInit();
        this.haloInit();

        this.getChildren().addAll(this.halo, this.background);
    }

    private void backgroundInit() {
        this.background = new Circle(REGULAR_POINT_RADIUS);
        this.background.getStyleClass().add(StylesheetsConstants.TERMINAL_BACKGROUND);
        this.isSelected.addListener((observable, oldValue, newValue) -> this.background.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SELECTED, newValue));
    }

    private void haloInit() {
        this.halo = new Rectangle(HALO_SIZE, HALO_SIZE);
        this.halo.visibleProperty().bind(this.isSelected);
        this.halo.getStyleClass().add(StylesheetsConstants.HALO);
        this.isFirstEndpoint.addListener((observable, oldValue, newValue) -> this.halo.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_FIRSTENDPOINT, newValue));
        this.isSecondEndpoint.addListener((observable, oldValue, newValue) -> this.halo.pseudoClassStateChanged(StylesheetsConstants.PSEUDO_CLASS_SECONDENDPOINT, newValue));
    }

    public void delete() {
        this.terminal.getLocation().getXProperty().unbind();
        this.terminal.getLocation().getYProperty().unbind();
        this.terminal = null;
        this.unselect();
    }

    @Deprecated // Bad practice
    public STBTerminal getTerminal() {
        return terminal;
    }

    @Deprecated // Bad practice
    public void setTerminal(STBTerminal terminal) {
        this.terminal = terminal;
        if (((EuclideanTerminal) this.terminal).type == STBTerminalType.STEINER_TERMINAL) this.background.setRadius(6);
        this.terminal.getLocation().getXProperty().bind(this.layoutXProperty());
        this.terminal.getLocation().getYProperty().bind(this.layoutYProperty());
//      TODO: if something must change in real time
//        ((PagePane) this.getParent()).algorithmInProgressProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                this.terminal.getLocation().getXProperty().unbind();
//                this.terminal.getLocation().getYProperty().unbind();
//                this.layoutXProperty().bind(this.terminal.getLocation().getXProperty());
//                this.layoutYProperty().bind(this.terminal.getLocation().getYProperty());
//            } else {
//                this.layoutXProperty().unbind();
//                this.layoutYProperty().unbind();
//                this.terminal.getLocation().getXProperty().bind(this.layoutXProperty());
//                this.terminal.getLocation().getYProperty().bind(this.layoutYProperty());
//            }
//        });
    }


    @Deprecated // Bad practice
    public STBTerminal genTerminal(double x, double y, STBGraph graph) {
        // TODO: get id
        this.setTerminal(new EuclideanTerminal(new EuclideanLocation(x, y), IdUtils.getTerminalId(graph)));
        return this.terminal;
    }

    public void select() {
        this.isSelected.set(true);
    }

    public void unselect() {
        this.isSelected.set(false);
        this.isFirstEndpoint.set(false);
        this.isSecondEndpoint.set(false);
    }

    public void setIsFirstEndpoint(boolean isFirstEndpoint) {
        this.isFirstEndpoint.set(isFirstEndpoint);
    }

    public void setIsSecondEndpoint(boolean isSecondEndpoint) {
        this.isSecondEndpoint.set(isSecondEndpoint);
    }
}
