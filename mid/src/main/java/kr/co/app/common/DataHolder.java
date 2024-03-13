package kr.co.app.common;

import org.springframework.stereotype.Component;

@Component
public class DataHolder {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
