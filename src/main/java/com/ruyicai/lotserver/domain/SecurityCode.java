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
@RooEntity(versionField="", table="vol_securitycode", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class SecurityCode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "mobileid")
	private String mobileid;
	
	@Column(name = "count")
	private Integer count;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "securitycode")
	private String securitycode;
	
	@Column(name = "createtime")
	private String createtime;
	
	
	public static List<SecurityCode> getList(String where, String orderby, List<Object> params) {
		TypedQuery<SecurityCode> q = entityManager().createQuery(
				"SELECT o FROM SecurityCode o " + where + orderby, SecurityCode.class);
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
