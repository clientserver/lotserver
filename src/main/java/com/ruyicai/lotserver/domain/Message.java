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
@RooEntity(versionField="", table="vol_message", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "keystr")
	private String keystr;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "memo")
	private String memo;
	
	
	public static List<Message> getList(String where, String orderby, List<Object> params) {
		TypedQuery<Message> q = entityManager().createQuery(
				"SELECT o FROM Message o " + where + orderby, Message.class);
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
