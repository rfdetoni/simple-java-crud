package com.test.simplecrud.utils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class DateUtils {
    private DateUtils(){}

    private static final String ZONE = "America/Sao_Paulo";

    public static LocalDate intantToLocalDate(Instant date) {
        try{
            return LocalDate.ofInstant( date, ZoneId.of(ZONE) );
        }catch (Exception e){
            return null;
        }
    }

    public static LocalDateTime intantToLocalDateTime(Instant date) {
        try{
            return LocalDateTime.ofInstant( date, ZoneId.of(ZONE) );
        }catch (Exception e){
            return null;
        }
    }
}
