package core.interfaces;

import java.util.List;

public interface STBPage<Component extends STBComponent> {

    List<Component> getAllComponents();
    boolean addComponent(Component component);
    boolean removeComponent(Component component);
}
