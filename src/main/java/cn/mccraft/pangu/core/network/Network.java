package cn.mccraft.pangu.core.network;

/**
 * @since 1.0.0
 * @author trychen
 */
public class Network {
    private static int discriminatorID = -1;

    public static int getNextID(){
        return discriminatorID++;
    }
}
