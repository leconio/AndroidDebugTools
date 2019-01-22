package io.lecon.debugtools.db;

import java.io.File;

public class DbBean {

    private String name;
    private String password;
    private File file;

    public DbBean(String name, String password, File file) {
        this.name = name;
        this.password = password;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
