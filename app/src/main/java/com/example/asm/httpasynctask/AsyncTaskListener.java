package com.example.asm.httpasynctask;

public interface AsyncTaskListener {
    void start();
    void end(Object result);
}
