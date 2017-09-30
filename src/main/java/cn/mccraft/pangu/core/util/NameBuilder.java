package cn.mccraft.pangu.core.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to build registry name
 *
 * @since .3
 */
public interface NameBuilder {
    /**
     * Building the registry name, liking [hello, world] to hello_world
     *
     * @param params all parts of name
     * @return built name
     */
    @Nonnull
    static String buildRegistryName(String... params) {
        StringBuilder stringBuilder = new StringBuilder(params[0]);
        String[] copied = Arrays.copyOfRange(params, 1, params.length);
        for (String s : copied) {
            stringBuilder.append('_');
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    /**
     * Building the registry name, liking [hello, world] to HelloWorld
     *
     * @param params all parts of name
     * @return built name
     */
    @Nonnull
    static String buildUnlocalizedName(String... params) {
        StringBuilder stringBuilder = new StringBuilder(params[0]);
        String[] copied = Arrays.copyOfRange(params, 1, params.length);
        for (String s : copied) {
            stringBuilder.append(Character.toUpperCase(s.charAt(0)));
            stringBuilder.append(s.substring(1));
        }
        return stringBuilder.toString();
    }

    /**
     * apart name like "helloWorld", "hello_world", "HELLO_WORLD" to ["hello", "world"]
     */
    static String[] apart(String name) {
        return name.contains("_") ? apartUnderline(name) : apartHump(name);
    }

    /**
     * apart hump to array, such as "helloWorld" 2 ["hello", "world"]
     */
    static String[] apartHump(String name) {
        return humpToUnderline(name).split("_");
    }

    /**
     * apart name like "hello_world", "HELLO_WORLD" (with underline separated) to ["hello", "world"]
     */
    static String[] apartUnderline(String name) {
        return name.toLowerCase().split("_");
    }

    /**
     * the pattern to check upper case letter
     */
    Pattern upperCasePattern = Pattern.compile("[A-Z]");

    /**
     * convert hump to underline format, such as "helloWorld" 2 "hello_world"
     */
    static String humpToUnderline(String str) {
        Matcher matcher = upperCasePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();

    }
}