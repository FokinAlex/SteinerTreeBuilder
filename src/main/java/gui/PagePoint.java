package gui;

import core.exceptions.IllegalLocationException;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBTerminal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PagePoint extends StackPane {

    public static final double REGULAR_POINT_RADIUS = 10.;
    public static final double HALO_BORDER_OFFSET = 4.;
    public static final double HALO_SIZE = REGULAR_POINT_RADIUS * 2 + HALO_BORDER_OFFSET * 2;

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

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
            ((PagePane) this.getParent()).select(this.isSelected.get() ? null : this);
        });

        backgroundInit();
        haloInit();

        this.getChildren().addAll(halo, background);
    }

    private void backgroundInit() {
        background = new Circle(REGULAR_POINT_RADIUS);
        // TODO: bind radius
        background.centerXProperty().bind(this.layoutXProperty());
        background.centerYProperty().bind(this.layoutYProperty());
        background.getStyleClass().add(StylesheetsConstants.TERMINAL_BACKGROUND);
    }

    private void haloInit() {
        halo = new Rectangle(HALO_SIZE, HALO_SIZE);
        halo.getStyleClass().add(StylesheetsConstants.TERMINAL_HALO);
        halo.visibleProperty().bind(this.isSelected);
        halo.xProperty().bind(this.layoutXProperty());
        halo.yProperty().bind(this.layoutYProperty());
        isSelected.addListener((observable, oldValue, newValue) -> halo.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, newValue));
    }

    @Deprecated // Bad practice
    public STBTerminal getTerminal() {
        return terminal;
    }

    @Deprecated // Bad practice
    public void setTerminal(STBTerminal terminal) {
        this.terminal = terminal;
        terminal.getLocation().getXProperty().bind(this.layoutXProperty());
        terminal.getLocation().getYProperty().bind(this.layoutYProperty());
    }

    @Deprecated // Bad practice
    public STBTerminal genTerminal(double x, double y) {
        try {
            this.setTerminal(new EuclideanTerminal(new EuclideanLocation(x, y)));
        } catch (IllegalLocationException e) {
            // Never be here
        }
        return this.terminal;
    }

    public void select() {
        this.isSelected.set(true);
    }

    public void unselect() {
        this.isSelected.set(false);
    }
}
