package cn.mccraft.pangu.core.client.tooltip;

public enum ToolTipType {
    NORMAL((byte) 0);
    private final byte id;

    ToolTipType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public static ToolTipType valueOf(byte id) {
        for (ToolTipType value : values()) {
            if (value.id == id) return value;
        }
        return NORMAL;
    }
}
