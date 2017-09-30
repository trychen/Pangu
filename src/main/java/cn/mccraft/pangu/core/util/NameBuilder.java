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

    static String[] apart(String name) {
        return name.contains("_")? apartUnderline(name) : apartHump(name);
    }

    static String[] apartHump(String name) {
        return humpToUnderline(name).split("_");
    }

    static String[] apartUnderline(String name) {
        return name.split("_");
    }

    Pattern upperCasePattern = Pattern.compile("[A-Z]");

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