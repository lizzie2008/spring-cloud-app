package tech.lancelot.shoppingcommon.utils;

import java.util.Random;

public class KeyUtil {

    /**
     * 主键生成工具
     * 格式：前缀+时间戳+随机数
     */
    public static synchronized String genUniqueKey(String prefix) {
        Random random = new Random();
        return prefix + System.currentTimeMillis() + String.format("%06d", random.nextInt(1000000));
    }
}
