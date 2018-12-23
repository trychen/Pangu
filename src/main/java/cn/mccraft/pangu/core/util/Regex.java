package cn.mccraft.pangu.core.util;

import javax.annotation.RegEx;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Regex {
    static String find(String str, @RegEx String key) {
        Matcher matcher = Pattern.compile(key).matcher(str);
        return matcher.find() ? matcher.group(1) : "";
    }
}
