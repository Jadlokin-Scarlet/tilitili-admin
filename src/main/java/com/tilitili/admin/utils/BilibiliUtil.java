package com.tilitili.admin.utils;

import com.tilitili.common.utils.Asserts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BilibiliUtil {
    private static final long xor = 177451812L;
    private static final long add = 8728348608L;
    private static final List<Integer> link = Arrays.asList(11,10,3,8,4,6);
    private static final List<String> list = Arrays.asList("fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF".split(""));
    private static final Map<String, Long> map = IntStream.range(0, list.size()).boxed().collect(Collectors.toMap(list::get, Long::valueOf));
    static {
        Collections.reverse(link);
    }
    public static Long converseAvToBv(String bv) {
        List<String> bvList = Arrays.asList(bv.split(""));
        Asserts.isTrue(bvList.size() == 12,"bv号长度应为12，不对劲");
        Long av = link.stream().map(bvList::get).map(map::get).reduce(0L, (a, b) -> a * list.size() + b);
        av = (av - add) ^ xor;
        Asserts.isTrue(av.toString().length() < 10,"av号长度超过9了，不对劲: " + av);
        return av;
    }

    public static void main(String[] args) {
        System.out.println(converseAvToBv("BV1np4y1h7GR"));
    }
}
