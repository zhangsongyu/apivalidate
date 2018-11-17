package com.api.validate.web.util;

import java.io.BufferedReader;
import java.util.stream.Collectors;

public class FileUtil {
    public static String readAllLinesWithStream(BufferedReader reader) {
        return reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
