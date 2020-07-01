package cn.mccraft.pangu.core.util;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.Load;
import io.netty.util.internal.ConcurrentSet;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ProgressManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LocalCache extends Thread {
    private static final Path CACHE_DIR = Loader.instance().getConfigDir().toPath().resolve("cache");
    private static final Path CACHE_MARK_FILE = Loader.instance().getConfigDir().toPath().resolve("panguLocalCache.csv");
    private static Map<String, Path> groups = new ConcurrentHashMap<>();
    private static Set<Path> usedFiles = new ConcurrentSet<>();
    private static Map<String, LocalDate> data = new ConcurrentHashMap<>();

    static {
        if (!Files.exists(CACHE_DIR)) {
            try {
                Files.createDirectory(CACHE_DIR);
            } catch (IOException e) {
                PanguCore.getLogger().info("Error while create cache dir", e);
            }
        }
    }

    @Load
    public static void init() {
        ProgressManager.ProgressBar bar = ProgressManager.push("Resolving Cache", 2);
        bar.step("Add Shutdown Hook");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LocalDate now = LocalDate.now();

                Files.walkFileTree(CACHE_DIR, new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (dir.equals(CACHE_DIR) || dir.getParent().equals(CACHE_DIR))
                            return FileVisitResult.CONTINUE;
                        if (dir.getParent().getParent().equals(CACHE_DIR)) {
                            String key = dir.getParent().getFileName() + ":" + dir.getFileName();
                            LocalDate fileTime = data.get(key);
                            if (fileTime == null) {
                                data.put(key, now);
                            } else {
                                if (ChronoUnit.DAYS.between(fileTime, now) > 20) {
                                    data.remove(key);
                                    Files.delete(dir);
                                }
                            }
                        }
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String key = file.getParent().getFileName() + ":" + file.getFileName();
                        LocalDate fileTime = data.get(key);
                        if (fileTime == null) {
                            data.put(key, now);
                        } else {
                            if (ChronoUnit.DAYS.between(fileTime, now) > 8) {
                                data.remove(key);
                                Files.delete(file);
                            }
                        }
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

                Files.delete(CACHE_MARK_FILE);
                Files.write(CACHE_MARK_FILE, data.entrySet().stream().map(e -> e.getKey() + "," + e.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()).collect(Collectors.toList()), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        try {
            LocalDate now = LocalDate.now();

            if (Files.exists(CACHE_MARK_FILE)) {
                List<String> lines = Files.readAllLines(CACHE_MARK_FILE, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] split = line.split(",");
                    if (split.length != 2) continue;
                    LocalDate localDate = Instant.ofEpochMilli(Long.parseLong(split[1])).atZone(ZoneId.systemDefault()).toLocalDate();
                    data.put(split[0], localDate);
                }
            }

            bar.step("Cleaning Invalid Cache");
            for (Map.Entry<String, LocalDate> e : data.entrySet()) {
                if (ChronoUnit.DAYS.between(e.getValue(), now) > 8) {
                    String[] split = e.getKey().split(":");
                    if (split.length != 2) continue;
                    String group = split[0];
                    String id = split[1];
                    Path path = get(group, id);
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                    data.remove(e.getKey());
                }
            }
        } catch (IOException e) {
            PanguCore.getLogger().info("Error while read cache file", e);
        }
        ProgressManager.pop(bar);
    }

    public static Path get(String group, String id) {
        return getGroup(group).resolve(id);
    }

    public static Path getGroup(String group) {
        Path path = groups.get(group);
        if (path == null) {
            path = CACHE_DIR.resolve(group);
            if (!Files.exists(path)) try {
                Files.createDirectory(path);
            } catch (IOException e) {
                PanguCore.getLogger().info("Error while create cache group dir: " + group, e);
                return null;
            }
            groups.put(group, path);
        }
        return path;
    }

    public static void markUsed(String group, String id) {
        String key = group + ":" + id;
        data.put(key, LocalDate.now());
    }

    public static void markGroupUsed(String group) {
        usedFiles.add(CACHE_DIR.resolve(group));
    }

    public static String findKeyFromPath(Path path) {
        Path relativize = CACHE_DIR.relativize(path);
        if (relativize.getNameCount() <= 1) {
            return null;
        }
        return relativize.getName(0) + ":" + relativize.getName(1);
    }

    @Deprecated
    public static void markFileUsed(Path usedFile) {
        String keyFromPath = findKeyFromPath(usedFile);
        if (keyFromPath != null) {
            data.put(keyFromPath, LocalDate.now());
        }
    }

    @Deprecated
    public static File getCachePath(String group, String id) {
        return get(group, id).toFile();
    }

    @Deprecated
    public static File getCacheGroup(String group) {
        return getGroup(group).toFile();
    }
}
