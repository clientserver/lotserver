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
@RooEntity(versionField="", table="vol_expertcode", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class ExpertCode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "messagecode")
	private String messagecode;
	
	@Column(name = "tophone")
	private String tophone;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "alertmessage")
	private String alertmessage;
	
	@Column(name = "buttontext")
	private String buttontext;
	
	@Column(name = "memo")
	private String memo;
	
	public static void findList(String where, String orderby,
			List<Object> params, Page<ExpertCode> page) {
		try {
			TypedQuery<ExpertCode> q = entityManager().createQuery(
					"SELECT o FROM ExpertCode o " + where + orderby, ExpertCode.class);
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
					"select count(o) from ExpertCode o " + where, Long.class);
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
	
}
