package com.rsm.common.vo;

import java.io.Serializable;

public class FileInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;
	private long fileSize;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
