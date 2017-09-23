package cn.mccraft.pangu.core.network;

/**
 * Created by trychen on 17/9/23.
 */
public class Network {
    private static int discriminatorID = -1;

    public static int getNextID(){
        return discriminatorID++;
    }
}
