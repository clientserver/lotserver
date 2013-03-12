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
@RooEntity(versionField="", table="lottypeinfo", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class LotTypeInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "lotno")
	private String lotno;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "introduce")
	private String introduce;
	
	@Column(name = "addawardstate")
	private String addawardstate;
	
	@Column(name = "salestate")
	private String salestate;
	
	
	public static List<LotTypeInfo> getList(String where, String orderby, List<Object> params) {
		TypedQuery<LotTypeInfo> q = entityManager().createQuery(
				"SELECT o FROM LotTypeInfo o " + where + orderby, LotTypeInfo.class);
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
