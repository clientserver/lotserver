package com.ruyicai.lotserver.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="timageconfig", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class TimageConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "productno")
	private String productno;
	
	@Column(name = "platform")
	private String platform;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "state")
	private String state;
	
	
	public static List<TimageConfig> getList(String where, String orderby, List<Object> params) {
		TypedQuery<TimageConfig> q = entityManager().createQuery(
				"SELECT o FROM TimageConfig o " + where + orderby, TimageConfig.class);
		if (null != params && !params.isEmpty()) {
			int index = 1;
			for (Object param : params) {
				q.setParameter(index, param);
				index = index + 1;
			}
		}
		return q.getResultList();
	}
	
}
