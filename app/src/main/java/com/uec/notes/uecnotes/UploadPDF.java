package com.uec.notes.uecnotes;

public class UploadPDF {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    private String name;
    private String url;
    private String sem;
    private String branch;

    public UploadPDF()
    {}
    public UploadPDF(String name,String url,String sem,String branch)
    {
        this.name=name;
        this.url=url;
        this.branch=branch;
        this.sem=sem;
    }
}
