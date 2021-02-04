package com.github.muirandy;

import com.github.muirandy.docs.living.api.Log;

public class MyLog extends Log {

    public MyLog(String message, String body) {
        super(message, body);
    }

    public MyLog() {
        super("", "");
    }

    public void setMessage(String message) {
    }
}
