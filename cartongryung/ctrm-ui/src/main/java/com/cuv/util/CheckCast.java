package com.cuv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckCast { // unchecked cast 경고 없애기 위함
    private static final Logger logger = LoggerFactory.getLogger(CheckCast.class);

    public static List<String> mapStrObjToListString(Map<String, Object> mapObj, String mapKey){
        List<String> convertedData = new ArrayList<>();
        try{
            if (mapObj.get(mapKey) instanceof List) {
                @SuppressWarnings("unchecked") List<String> listObj = (List<String>)mapObj.get(mapKey);
                for (Object item : listObj) {
                    if (item instanceof String) {
                        convertedData.add((String) item);
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return convertedData;
    }

    public static List<String> objToListString(Object obj){
        List<String> convertedData = new ArrayList<>();
        try{
            if (obj instanceof List) {
                @SuppressWarnings("unchecked") List<String> listObj = (List<String>)obj;
                for (Object item : listObj) {
                    if (item instanceof String) {
                        convertedData.add((String) item);
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return convertedData;
    }

    public static List<Long> objToListLong(Object obj){
        List<Long> convertedData = new ArrayList<>();
        try{
            if (obj instanceof List) {
                @SuppressWarnings("unchecked") List<String> listObj = (List<String>)obj;
                for (Object item : listObj) {
                    if (item instanceof Long) {
                        convertedData.add((Long) item);
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return convertedData;
    }
/*
    public static Map<String, Object> mapStrObjToMapStrObj(Map<String, Object> mapObj, String mapKey){
        Map<String, Object> convertedData = new LinkedHashMap<String, Object>();
        try{
            if (mapObj.get(mapKey) instanceof Map) {
                @SuppressWarnings("unchecked") convertedData.cast(mapObj.get(mapKey));
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return convertedData;
    }

    // public static String getClassTypeFromParam()*/

}
