package com.tilitili.admin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.logging.log4j.util.Strings.isBlank;

public class StringUtil {

    public static List<Long> splitNumberList(String listStr) {
        if (isBlank(listStr)) {
            return new ArrayList<>();
        }
        String[] strList = listStr.split(",");
        return Stream.of(strList).map(Long::valueOf).collect(Collectors.toList());
    }
}
