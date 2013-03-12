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
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.ruyicai.lotserver.util.common.Page;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="vol_activity", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "productno")
	private String productno;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "introduce")
	private String introduce;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "activitytime")
	private String activitytime;
	
	@Column(name = "createtime")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "updatetime")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatetime;
	
	@Column(name = "isend")
	private String isend;
	
	
	public static void findList(String where, String orderby, List<Object> params, Page<Activity> page) {
		try {
			TypedQuery<Activity> q = entityManager().createQuery(
					"SELECT o FROM Activity o " + where + orderby, Activity.class);
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
					"select count(o) from Activity o " + where, Long.class);
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
	
	public static Integer findCount(String where, List<Object> params) {
		TypedQuery<Long> q = entityManager().createQuery(
				"select count(o) from Activity o " + where, Long.class);
		if (null != params && !params.isEmpty()) {
			int index = 1;
			for (Object param : params) {
				q.setParameter(index, param);
				index = index + 1;
			}
		}
		return q.getSingleResult().intValue();
	}
	
}
