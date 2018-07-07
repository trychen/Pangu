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
 * Registry Name 构造器
 *
 * @since 1.0.0.3
 */
public interface NameBuilder {
    /**
     * Building the registry name, liking [hello, world] to hello_world
     *
     * 将字符串数组用下划线拼合，例如：[hello, world] 转换成 hello_world
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
     * Building the unlocalized name, liking [hello, world] to helloWorld
     *
     * 将字符串数组转换成小驼峰写法的字符串，例如：[hello, world] 转换成 helloWorld
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
     *
     * 拆分类似小驼峰命名法或下划线命名法的字符串的每个单词拆分，并转换成小写，例如
     * "helloWorld"、"hello_world"、"HELLO_WORLD" 转换成 ["hello", "world"]
     */
    static String[] apart(String name) {
        return name.contains("_") ? apartUnderline(name) : apartHump(name);
    }

    /**
     * apart hump to array, such as "helloWorld" 2 ["hello", "world"]
     *
     * 拆分小驼峰命名法的字符串，例如："helloWorld" 转换成 ["hello", "world"]
     */
    static String[] apartHump(String name) {
        return humpToUnderline(name).split("_");
    }

    /**
     * apart name like "hello_world", "HELLO_WORLD" (with underline separated) to ["hello", "world"]
     *
     * 拆分下划线命名法的字符串为数组，例如："hello_world", "HELLO_WORLD" 转换成 ["hello", "world"]
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
     *
     * 转换小驼峰命名法的字符串为下划线命名法，例如："helloWorld" 转换成 "hello_world"
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