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
@RooEntity(versionField="", table="giftmessage", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class GiftMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "createtime")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "updatetime")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatetime;
	
	@Column(name = "state")
	private String state;
	
	
	public static void findList(String where, String orderby, List<Object> params, Page<GiftMessage> page) {
		try {
			TypedQuery<GiftMessage> q = entityManager().createQuery(
					"SELECT o FROM GiftMessage o " + where + orderby, GiftMessage.class);
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
					"select count(o) from GiftMessage o " + where, Long.class);
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
	
	public static List<GiftMessage> getList(String where, String orderby, List<Object> params) {
		TypedQuery<GiftMessage> q = entityManager().createQuery(
				"SELECT o FROM GiftMessage o " + where + orderby, GiftMessage.class);
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
