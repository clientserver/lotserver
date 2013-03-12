package com.ruyicai.lotserver.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="tgrayupgrade", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class TgrayUpgrade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "imei")
	private String imei;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "createtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "updatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatetime;
	
	@Column(name = "state")
	private String state;
	
	
	public static List<TgrayUpgrade> getList(String where, String orderby, List<Object> params) {
		TypedQuery<TgrayUpgrade> q = entityManager().createQuery(
				"SELECT o FROM TgrayUpgrade o " + where + orderby, TgrayUpgrade.class);
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
