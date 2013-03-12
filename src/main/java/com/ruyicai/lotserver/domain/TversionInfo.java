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
import com.ruyicai.lotserver.util.common.Page;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="tversioninfo", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class TversionInfo {

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
	
	@Column(name = "versionintroduce")
	private String versionintroduce;
	
	@Column(name = "upgradeurl")
	private String upgradeurl;
	
	@Column(name = "upgradedescription")
	private String upgradedescription;
	
	
	public static void findList(String where, String orderby,
			List<Object> params, Page<TversionInfo> page) {
		try {
			TypedQuery<TversionInfo> q = entityManager().createQuery(
					"SELECT o FROM TversionInfo o " + where + orderby, TversionInfo.class);
			if (null != params && !params.isEmpty()) {
				int index = 1;
				for (Object param : params) {
					q.setParameter(index, param);
					index = index + 1;
				}
			}
			q.setFirstResult(page.getPageIndex() * page.getMaxResult())
					.setMaxResults(page.getMaxResult());
			page.setList(q.getResultList());
			TypedQuery<Long> totalQ = entityManager().createQuery(
					"select count(o) from TversionInfo o " + where, Long.class);
			if (null != params && !params.isEmpty()) {
				int index = 1;
				for (Object param : params) {
					totalQ.setParameter(index, param);
					index = index + 1;
				}
			}
			page.setTotalResult(totalQ.getSingleResult().intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<TversionInfo> getList(String where, String orderby, List<Object> params) {
		TypedQuery<TversionInfo> q = entityManager().createQuery(
				"SELECT o FROM TversionInfo o " + where + orderby, TversionInfo.class);
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
