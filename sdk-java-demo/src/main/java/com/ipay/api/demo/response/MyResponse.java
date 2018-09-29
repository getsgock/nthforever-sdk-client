package com.ipay.api.demo.response;

import com.ipay.api.common.internal.mapping.ApiField;
import com.ipay.api.common.response.IpayResponse;

public class MyResponse extends IpayResponse {

    @ApiField("school")
    private String school;

    @ApiField("now")
    private Long now;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "MyResponse{" +
                "school='" + school + '\'' +
                ", now=" + now +
                "} " + super.toString();
    }
}
