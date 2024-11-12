package com.bank.account.core.common.utls;

import java.util.Random;
import java.util.UUID;

public class AppUtils {
    public static Long generateUid(){

        return generateRandom(12);
    }

    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
}
