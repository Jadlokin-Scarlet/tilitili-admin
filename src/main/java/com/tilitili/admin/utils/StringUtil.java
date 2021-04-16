package com.tilitili.admin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static String bigNumberFormat(String number) {
        if (number == null) {
            return null;
        }
        String result = "";
        if (number.length() > 6) {
            result += number.substring(0, number.length() - 6);
            result += ",";
            result += number.substring(number.length() - 6, number.length() - 3);
            result += ",";
            result += number.substring(number.length() - 3);
        } else if (number.length() > 3) {
            result += number.substring(0, number.length() - 3);
            result += ",";
            result += number.substring(number.length() - 3);
        } else {
            result += number;
        }
        return result;
    }

    public static String bigNumberFormat(Long number) {
        return bigNumberFormat(number.toString());
    }

    public static String bigNumberFormat(Integer number) {
        return bigNumberFormat(number.toString());
    }

    public static String matcherGroupOne(String regx, String source) {
        Matcher matcher = Pattern.compile(regx).matcher(source);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}