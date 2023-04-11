/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tech.fate.portal.util;

import com.google.common.collect.Lists;
import com.tech.fate.portal.model.Chunk;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author Marco Polo
 **/
@Slf4j
public class FileUtils {

    public static String generatePath(String uploader, Chunk chunk) throws Exception {
        StringBuilder localPath = new StringBuilder();
        localPath.append(uploader).append("/").append(chunk.getIdentifier());
        Path path = Paths.get(localPath.toString());
        if (!Files.isWritable(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("make local file path error", e);
                throw new Exception(e.getMessage());
            }
        }
        return localPath.append("/")
                .append(chunk.getFileName())
                .append("-")
                .append(chunk.getChunkNumber())
                .toString();
    }

    public static void chunkMerge(String targetFile, String folder, String fileName) throws Exception {
        try {
            Files.createFile(Paths.get(targetFile));
        } catch (IOException e) {
            log.error("merge file error", e);
            throw new Exception(e);
        }
        try (Stream<Path> stream = Files.list(Paths.get(folder))) {
            stream.filter(path -> !path.getFileName().equals(fileName))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int p1Index = p1.indexOf("-");
                        int p2Index = p2.indexOf("-");
                        return Integer.valueOf(p2.substring(p2Index)).compareTo(Integer.valueOf(p1.substring(p1Index)));
                    }).forEach(path -> {
                        try {
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            Files.delete(path);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

    public static boolean checkFileType(String fileName, List<String> fileTypes) {
        String fileType = getFileExtension(fileName);
        return fileTypes.contains(fileType);
    }

    public static boolean checkFileType(String fileName) {
        List<String> fileTypes = Lists.newArrayList("csv");
        return checkFileType(fileName, fileTypes);
    }
}
