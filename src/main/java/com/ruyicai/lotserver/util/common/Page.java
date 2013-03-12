package com.ruyicai.lotserver.util.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.tostring.RooToString;

@RooToString
public class Page<E> {

	private Integer pageIndex = 0; // 第几页
	@SuppressWarnings("unused")
	private Integer totalPage; // 总页数
	private Integer maxResult = 15; // 每页显示多少条
	private Integer totalResult = 0; // 总记录数
	private List<E> list; // 结果集
	private Object value; // Object
	private String orderBy = null; // 排序字段名称
	private String orderDir = null; // 排序循序

	public Page() {
		super();
	}

	public Page(int pageIndex, int maxResult) {
		super();
		this.pageIndex = pageIndex;
		this.maxResult = maxResult;
	}

	public Page(Integer pageIndex, Integer maxResult, String orderBy,
			String orderDir) {
		super();
		this.pageIndex = pageIndex;
		this.maxResult = maxResult;
		this.orderBy = orderBy;
		this.orderDir = orderDir;
	}

	public Page(Integer pageIndex, Integer maxResult, Integer totalResult,
			String orderBy, String orderDir, List<E> list) {
		super();
		this.pageIndex = pageIndex;
		this.maxResult = maxResult;
		this.totalResult = totalResult;
		this.orderBy = orderBy;
		this.orderDir = orderDir;
		this.list = list;
	}

	/**
	 * 当前页数
	 */
	public Integer getCurrentPageNo() {
		if (pageIndex == null || maxResult == null || maxResult == 0) {
			return 1;
		}
		return pageIndex / maxResult + 1;
	}

	public Integer getPageIndex() {

		return null == pageIndex ? 0 : pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getTotalPage() {
		return totalResult % getMaxResult() == 0 ? totalResult / getMaxResult()
				: totalResult / getMaxResult() + 1;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getMaxResult() {
		return null == maxResult ? 15 : maxResult;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public Integer getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(Integer totalResult) {
		this.totalResult = totalResult;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(String orderDir) {
		String lowcaseOrderDir = StringUtils.lowerCase(orderDir);
		this.orderDir = lowcaseOrderDir;
	}

	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}

	public List<Sort> fetchSort() {
		List<Sort> orders = new ArrayList<Sort>();
		if (StringUtils.isBlank(orderBy) || StringUtils.isBlank(orderDir)) {
			return orders;
		}
		String[] orderBys = StringUtils.split(orderBy, ',');
		String[] orderDirs = StringUtils.split(orderDir, ',');

		for (int i = 0; i < orderBys.length; i++) {
			orders.add(new Sort(orderBys[i], orderDirs[i]));
		}
		return orders;
	}

	public static class Sort {
		public static final String ASC = "asc";
		public static final String DESC = "desc";

		private final String property;
		private final String dir;

		public Sort(String property, String dir) {
			this.property = property;
			this.dir = dir;
		}

		public String getProperty() {
			return property;
		}

		public String getDir() {
			return dir;
		}
	}
}
