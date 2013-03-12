package com.ruyicai.lotserver.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="exceptionmessage", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class ExceptionMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "platform")
	private String platform;
	
	@Column(name = "softwareversion")
	private String softwareversion;
	
	@Column(name = "coopid")
	private String coopid;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "userno")
	private String userno;
	
	@Column(name = "mobileid")
	private String mobileid;
	
	@Column(name = "createtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;
	
}
