package io.http;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Integer[] a = new Integer[3];
        a[0] = 10;
        a[2] = 8;
        a[1] = Integer.MAX_VALUE;
        Arrays.sort(a);
        for (int i=0; i < a.length; i++) {
            System.out.printf("a[%d] = %d\n", i, a[i]);
        }
    }
}
