package com.test.simplecrud.utils;

import java.util.UUID;

public class TokenUtils {

    public static String generateNewToken(){
        return UUID.randomUUID().toString().concat( UUID.randomUUID().toString() );
    }
}
