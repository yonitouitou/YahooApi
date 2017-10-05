package com.tradair.mock.util;

public class FixUtil {

    public static String getSymbolWithSlash(String symbol) {
        String ccy1 = symbol.substring(0, 3);
        String ccy2 = symbol.substring(3, 6);
        return ccy1.concat("/").concat(ccy2);
    }
}
