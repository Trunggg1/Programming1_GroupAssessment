package Container;

import Menu.PortManagerMenu;

public enum ContainerType { // Set enum for container type
    DRY_STORAGE("Dry storage"),
    OPEN_TOP("Open top"),
    OPEN_SIDE("Open side"),
    REFRIGERATED("Refrigerated"),
    LIQUID("Liquid");

    private final String type;

    ContainerType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

