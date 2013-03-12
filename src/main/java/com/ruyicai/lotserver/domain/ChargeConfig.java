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
@RooEntity(versionField="", table="chargeconfig", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class ChargeConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "bankname")
	private String bankname;
	
	@Column(name = "cardtype")
	private String cardtype;
	
	@Column(name = "support")
	private String support;
	
	@Column(name = "state")
	private String state;
	
	
	public static List<ChargeConfig> getList(String where, String orderby, List<Object> params) {
		TypedQuery<ChargeConfig> q = entityManager().createQuery(
				"SELECT o FROM ChargeConfig o " + where + orderby, ChargeConfig.class);
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
