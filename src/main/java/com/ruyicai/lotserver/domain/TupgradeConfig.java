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
@RooEntity(versionField="", table="tupgradeconfig", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class TupgradeConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "productno")
	private String productno;
	
	@Column(name = "platform")
	private String platform;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "state")
	private String state;
	
	
	public static List<TupgradeConfig> getList(String where, String orderby, List<Object> params) {
		TypedQuery<TupgradeConfig> q = entityManager().createQuery(
				"SELECT o FROM TupgradeConfig o " + where + orderby, TupgradeConfig.class);
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
