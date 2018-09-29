package com.ipay.api.common.internal.util.json;

public class StdoutStreamErrorListener extends BufferErrorListener {
    
    public void end() {
        System.out.print(buffer.toString());
    }
}
