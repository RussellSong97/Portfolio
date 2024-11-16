package com.cuv.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Component
@Service
@RequiredArgsConstructor
public class EncodeData {

    /**
     * 암호화
     * @param data
     * @return
     */
    public static String encode(String data) throws UnsupportedEncodingException {
        return SeedCBC.encrypt(data);
    }

    /**
     * 복호화
     * @param data
     * @return
     */
    public static String decode(String data) throws UnsupportedEncodingException {
        return SeedCBC.decrypt(data);
    }
}
