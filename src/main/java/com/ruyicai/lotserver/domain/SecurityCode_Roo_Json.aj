// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.domain;

import com.ruyicai.lotserver.domain.SecurityCode;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect SecurityCode_Roo_Json {
    
    public String SecurityCode.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static SecurityCode SecurityCode.fromJsonToSecurityCode(String json) {
        return new JSONDeserializer<SecurityCode>().use(null, SecurityCode.class).deserialize(json);
    }
    
    public static String SecurityCode.toJsonArray(Collection<SecurityCode> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<SecurityCode> SecurityCode.fromJsonArrayToSecurityCodes(String json) {
        return new JSONDeserializer<List<SecurityCode>>().use(null, ArrayList.class).use("values", SecurityCode.class).deserialize(json);
    }
    
}