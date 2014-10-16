package com.stinfo.pushme.rest.entity;

import java.io.File;
import java.util.HashMap;

import com.stinfo.pushme.common.AppConstant;

/**
 * 上传图片
 * @author lenovo
 */
public class PostUploadPicReq  {

	private File uploadpic;

	public File getUploadpic() {
		return uploadpic;
	}

	public void setUploadpic(File uploadpic) {
		this.uploadpic = uploadpic;
	}

	public String getPath() {
		return AppConstant.School_Platform_BASE_URL + "behavior/uploadPic";
	}

}
