package cn.mccraft.pangu.core.network;

/**
 * @since 1.0.0
 * @author trychen
 */
public enum Network {
    INSTANCE;

    public static final int SERVER_KEY_MESSAGE_ID = 0;
    public static final int CLIENT_KEY_MESSAGE_ID = 1;
    public static final int TOOLTIP_MESSAGE_ID = 2;

    private int discriminatorID = 2;

    public int getNextID(){
        return discriminatorID++;
    }
}
