package com.cuv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CheckDevice { // unchecked cast 경고 없애기 위함
    private static final Logger logger = LoggerFactory.getLogger(CheckDevice.class);

    public static Boolean findDevice(String header){
        try{
            return header.contains("mobile");
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

}