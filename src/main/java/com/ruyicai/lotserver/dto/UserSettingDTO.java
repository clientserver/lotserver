package com.ruyicai.lotserver.dto;

import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

@RooJson
@RooJavaBean
public class UserSettingDTO {

	private UserSMSTiming userSMSTiming;

	private List<SendChannel> sendChannels;

}
