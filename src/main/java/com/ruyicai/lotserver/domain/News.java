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

import com.ruyicai.lotserver.util.common.Page;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="vol_news", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "productno")
	private String productno;
	
	@Column(name = "vol_typeid_fk")
	private String vol_typeid_fk;
	
	@Column(name = "lotno")
	private String lotno;
	
	@Column(name = "vol_title")
	private String vol_title;
	
	@Column(name = "vol_content")
	private String vol_content;
	
	@Column(name = "createtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;
	
	@Column(name = "updatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatetime;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "state")
	private String state;
	
	
	public static void findList(String where, String orderby,
			List<Object> params, Page<News> page) {
		try {
			TypedQuery<News> q = entityManager().createQuery(
					"SELECT o FROM News o " + where + orderby, News.class);
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
					"select count(o) from News o " + where, Long.class);
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
	
	public static List<News> getList(String where, String orderby, List<Object> params) {
		TypedQuery<News> q = entityManager().createQuery(
				"SELECT o FROM News o " + where + orderby, News.class);
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
