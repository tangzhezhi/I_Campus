package com.stinfo.pushme.util;

public class Page {

	private int recordIndex;
	private int totalCount;
	private int pageSize;

	public Page() {
		this.recordIndex = 0;
		this.totalCount = -1;
		this.pageSize = 15;
	}

	public void init(int pageSize) {
		this.recordIndex = 0;
		this.totalCount = -1;
		this.pageSize = pageSize;
	}

	public int getRecordIndex() {
		return recordIndex;
	}

	public int getNextBegin() {
		return recordIndex + 1;
	}

	public int getNextEnd() {
		if (totalCount == -1) {
			return pageSize;
		} else if ((recordIndex + pageSize) < totalCount) {
			return recordIndex + pageSize;
		} else {
			return totalCount;
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void addIndex(int count) {
		recordIndex += count;
	}

	public boolean hasNextPage() {
		return (recordIndex < totalCount);
	}
}