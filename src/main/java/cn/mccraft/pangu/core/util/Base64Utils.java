package cn.mccraft.pangu.core.util;

import java.util.Base64;

public interface Base64Utils {
    static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = Base64.getEncoder().encodeToString(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
    }

    static byte[] safeUrlBase64Decode(final String safeBase64Str){
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length()%4;
        if(mod4 > 0){
            base64Str = base64Str + "====".substring(mod4);
        }
        return Base64.getDecoder().decode(base64Str);
    }
}
