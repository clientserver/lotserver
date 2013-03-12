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
@RooEntity(versionField="", table="tbl_coop", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class Coop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "coopname")
	private String coopname;
	
	@Column(name = "coopid")
	private Integer coopid;
	
	@Column(name = "rate")
	private Integer rate;
	
	@Column(name = "productno")
	private String productno;
	
	
	public static List<Coop> getList(String where, String orderby, List<Object> params) {
		TypedQuery<Coop> q = entityManager().createQuery(
				"SELECT o FROM Coop o " + where + orderby, Coop.class);
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
