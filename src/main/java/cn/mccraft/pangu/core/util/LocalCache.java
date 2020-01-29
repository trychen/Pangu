package cn.mccraft.pangu.core.util;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.Load;
import io.netty.util.internal.ConcurrentSet;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCache extends Thread {
    private static File CACHE_DIR = new File(Loader.instance().getConfigDir(), "cache");
    private static Map<String, File> groups = new ConcurrentHashMap<>();
    private static Set<Path> usedFiles = new ConcurrentSet<>();

    static {
        if (!CACHE_DIR.exists()) {
            CACHE_DIR.mkdir();
        }
    }

    @Load
    public static void init() {
        Runtime.getRuntime().addShutdownHook(new LocalCache());
    }

    public static File getCachePath(String group, String id) {
        File groupFile = groups.get(group);
        if (groupFile == null) {
            groupFile = new File(CACHE_DIR, group);
            groupFile.mkdir();
            groups.put(group, groupFile);
        }
        return new File(groupFile, id);
    }

    public static File getCacheGroup(String group) {
        File groupFile = groups.get(group);
        if (groupFile == null) {
            groupFile = new File(CACHE_DIR, group);
            groupFile.mkdir();
        }
        return groupFile;
    }

    public static void markFileUsed(Path usedFile) {
        usedFiles.add(usedFile);
    }

    public static void markGroupUsed(String group) {
        usedFiles.add(new File(CACHE_DIR, group).toPath());
    }

    @Override
    public void run() {
        try {
            Collection<File> values = groups.values();
            Files.walkFileTree(CACHE_DIR.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    for (Path usedFile : usedFiles) {
                        if (usedFile.equals(dir)) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                    }
                    if (dir.equals(CACHE_DIR.toPath()) || values.contains(dir.toFile()))
                        return FileVisitResult.CONTINUE;
                    FileUtils.deleteDirectory(dir.toFile());
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    for (Path usedFile : usedFiles) {
                        if (usedFile.equals(file)) {
                            return FileVisitResult.CONTINUE;
                        }
                    }
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            PanguCore.getLogger().error("Error while delete unused cache", e);
        }
    }
}
