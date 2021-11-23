package com.example.pocketide.Models;

import java.io.File;

public class FileModel {
    String file_name,date;

    public FileModel(String file_name, String date){
        this.file_name=file_name;
        this.date=file_name;
    }

    public FileModel() {
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
