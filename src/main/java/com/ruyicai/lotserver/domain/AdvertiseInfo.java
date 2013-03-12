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
@RooEntity(versionField="", table="advertiseinfo", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class AdvertiseInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "mac")
	private String mac;
	
	@Column(name = "appid")
	private String appid;
	
	@Column(name = "advertiseid")
	private String advertiseid;
	
	@Column(name = "drkey")
	private String drkey;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "createtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "updatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatetime;
	
	@Column(name = "state")
	private String state;
	
	
	public static List<AdvertiseInfo> getList(String where, String orderby, List<Object> params) {
		TypedQuery<AdvertiseInfo> q = entityManager().createQuery(
				"SELECT o FROM AdvertiseInfo o " + where + orderby, AdvertiseInfo.class);
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
