package com.huawei.testLambda;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Test {
    public static void main(String[] args) {
        String gensalt = BCrypt.gensalt(12);
        System.out.println("盐:"+gensalt);
        //基于当前的盐对密码进行加密
        String saltPassword = BCrypt.hashpw("123456", gensalt);
        System.out.println("加密后的密文:"+saltPassword);
        //解密
        boolean checkpw = BCrypt.checkpw("123456", saltPassword);
        System.out.println("密码校验结果:"+checkpw);
    }
}
