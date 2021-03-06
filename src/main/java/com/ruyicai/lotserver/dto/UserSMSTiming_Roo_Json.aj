// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.dto;

import com.ruyicai.lotserver.dto.UserSMSTiming;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect UserSMSTiming_Roo_Json {
    
    public String UserSMSTiming.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static UserSMSTiming UserSMSTiming.fromJsonToUserSMSTiming(String json) {
        return new JSONDeserializer<UserSMSTiming>().use(null, UserSMSTiming.class).deserialize(json);
    }
    
    public static String UserSMSTiming.toJsonArray(Collection<UserSMSTiming> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<UserSMSTiming> UserSMSTiming.fromJsonArrayToUserSMSTimings(String json) {
        return new JSONDeserializer<List<UserSMSTiming>>().use(null, ArrayList.class).use("values", UserSMSTiming.class).deserialize(json);
    }
    
}
