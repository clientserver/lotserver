// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import java.lang.String;

privileged aspect Coop_Roo_ToString {
    
    public String Coop.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Coopid: ").append(getCoopid()).append(", ");
        sb.append("Coopname: ").append(getCoopname()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Productno: ").append(getProductno()).append(", ");
        sb.append("Rate: ").append(getRate());
        return sb.toString();
    }
    
}