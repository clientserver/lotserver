// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import java.lang.String;

privileged aspect AdvertiseInfo_Roo_ToString {
    
    public String AdvertiseInfo.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Advertiseid: ").append(getAdvertiseid()).append(", ");
        sb.append("Appid: ").append(getAppid()).append(", ");
        sb.append("Createtime: ").append(getCreatetime()).append(", ");
        sb.append("Drkey: ").append(getDrkey()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Mac: ").append(getMac()).append(", ");
        sb.append("Source: ").append(getSource()).append(", ");
        sb.append("State: ").append(getState()).append(", ");
        sb.append("Updatetime: ").append(getUpdatetime());
        return sb.toString();
    }
    
}