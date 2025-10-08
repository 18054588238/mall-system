package com.personal.mall.seckill.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 获取近dayNum天的起止日期时间
    public static String[] getLatestDays(long dayNum){
        String[] days = new String[2];
        LocalDate now = LocalDate.now();
        days[0] = now.atStartOfDay().format(dateTimeFormatter);
        days[1] = now.plusDays(dayNum-1).atTime(LocalTime.MAX).format(dateTimeFormatter);
        return days;
    }
}
