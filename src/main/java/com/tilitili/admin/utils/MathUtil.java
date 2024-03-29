package com.tilitili.admin.utils;

import java.math.BigDecimal;

public class MathUtil {
    private MathUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String formatByScale(long a, int scale) {
        BigDecimal b = BigDecimal.TEN.pow(scale);
        return new BigDecimal(a).divide(b, scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }
}
