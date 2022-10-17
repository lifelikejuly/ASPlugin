package com.vdian.wddevplugin.tools.finduseless;

import java.io.File;

public class OutPutInfo {

    String content;

    File file;

    public OutPutInfo() {
    }

    public OutPutInfo(String content) {
        this.content = content;
    }

    public OutPutInfo(String content, File file) {
        this.content = content;
        this.file = file;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
