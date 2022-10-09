package com.utils;

/**
 * Created by KingsLanding on 2022/9/16 17:59
 */
public class Sleep {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
