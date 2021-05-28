package com.devhow.identity.user;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeUtil {

    public Timestamp now() {
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }
}
