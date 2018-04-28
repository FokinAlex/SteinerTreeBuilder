package gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PagePoint extends StackPane {

    private static final double REGULAR_POINT_RADIUS = 10.;
    private static final double HALO_BORDER_OFFSET = 3.;
    private static final double HALO_SIZE = REGULAR_POINT_RADIUS * 2 + HALO_BORDER_OFFSET * 2;
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    private Circle background;
    private Rectangle halo;

    PagePoint(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setOnMouseClicked(event -> ((PagePane) this.getParent()).select(this.isSelected.get() ? null : this));

        backgroundInit();
        haloInit();

        this.getChildren().addAll(halo, background);
    }

    private void backgroundInit() {
        background = new Circle(REGULAR_POINT_RADIUS);
        // TODO: bind radius
        background.getStyleClass().add(StylesheetsConstants.TERMINAL_BACKGROUND);
    }

    private void haloInit() {
        halo = new Rectangle(
                -REGULAR_POINT_RADIUS + HALO_BORDER_OFFSET,
                -REGULAR_POINT_RADIUS + HALO_BORDER_OFFSET,
                HALO_SIZE,
                HALO_SIZE
        );
        halo.getStyleClass().add(StylesheetsConstants.TERMINAL_HALO);
        halo.visibleProperty().bind(this.isSelected);
        isSelected.addListener((observable, oldValue, newValue) -> halo.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, newValue));
    }


    public void select() {
        this.isSelected.set(true);
    }

    public void unselect() {
        this.isSelected.set(false);
    }
}
