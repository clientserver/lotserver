// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import java.lang.String;

privileged aspect LotTypeInfo_Roo_ToString {
    
    public String LotTypeInfo.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Addawardstate: ").append(getAddawardstate()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Introduce: ").append(getIntroduce()).append(", ");
        sb.append("Lotno: ").append(getLotno()).append(", ");
        sb.append("Salestate: ").append(getSalestate()).append(", ");
        sb.append("Title: ").append(getTitle());
        return sb.toString();
    }
    
}