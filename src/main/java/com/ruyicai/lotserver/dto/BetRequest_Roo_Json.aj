// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.lotserver.dto;

import com.ruyicai.lotserver.dto.BetRequest;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect BetRequest_Roo_Json {
    
    public String BetRequest.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static BetRequest BetRequest.fromJsonToBetRequest(String json) {
        return new JSONDeserializer<BetRequest>().use(null, BetRequest.class).deserialize(json);
    }
    
    public static String BetRequest.toJsonArray(Collection<BetRequest> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<BetRequest> BetRequest.fromJsonArrayToBetRequests(String json) {
        return new JSONDeserializer<List<BetRequest>>().use(null, ArrayList.class).use("values", BetRequest.class).deserialize(json);
    }
    
}
