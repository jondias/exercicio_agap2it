package com.exercicio.jonathan.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="metricModel")
public class MetricRequest {
    
    private String fileDate;

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

}
