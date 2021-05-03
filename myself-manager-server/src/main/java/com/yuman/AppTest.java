package com.yuman;


import java.util.Random;

public class AppTest {


    public static void main(String[] args) {
        test();
    }

    public static void test() {
        Random random1 = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 7; i++) {
            Integer random = random1.nextInt(7) + 1;
            if (sb.indexOf(random.toString()) != -1) {
                i--;
                continue;
            }
            sb.append(random);
            System.out.print(random + "  ");
        }
        System.out.println();
    }

    public static void testA() {
        Random random1 = new Random();
        Integer random = random1.nextInt(7) + 1;
        System.out.println(random);

    }
}
