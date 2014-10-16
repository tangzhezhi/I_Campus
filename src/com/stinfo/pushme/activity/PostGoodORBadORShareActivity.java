package com.stinfo.pushme.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stinfo.pushme.BaseActionBarActivity;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant.Ack;
import com.stinfo.pushme.common.AppConstant.DeedType;
import com.stinfo.pushme.common.AppConstant.PhotoType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.entity.Parent;
import com.stinfo.pushme.entity.Teacher;
import com.stinfo.pushme.entity.UserInfo;
import com.stinfo.pushme.rest.MyStringRequest;
import com.stinfo.pushme.rest.RequestController;
import com.stinfo.pushme.rest.entity.BaseResponse;
import com.stinfo.pushme.rest.entity.PostGoodOrBadOrShareReq;
import com.stinfo.pushme.util.FileUtils;
import com.stinfo.pushme.util.MessageBox;
import com.stinfo.pushme.view.SelectPicPopupWindow;

public class PostGoodORBadORShareActivity extends BaseActionBarActivity {
	private static final String TAG = "PostGoodORBadORShareActivity";
	
	private ProgressDialog prgDialog = null;
	private EditText etContent;
	private EditText etTitle;
	
	private ImageView btnPic1;
	private ImageView btnPic2;
	private ImageView btnPic3;
	private ImageView btnPic4;
	private int currentBtn = 1;
	private List<String> picUrls = new ArrayList<String>();
	//模块类型(闪光台1,曝光台2,爱心分享3)
	private String plates = null;
	 //自定义的弹出框类  
    private SelectPicPopupWindow menuWindow; 
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_deed);
		Intent intent = this.getIntent();
		plates = intent.getStringExtra("type");
		ActionBar bar = getSupportActionBar();
		if(plates.equals(DeedType.good)){
			bar.setTitle(getResources().getString(R.string.good_deed_show));
		}else if(plates.equals(DeedType.bad)){
			bar.setTitle(getResources().getString(R.string.bad_deed_show));
		}else if(plates.equals(DeedType.lover_share)){
			bar.setTitle(getResources().getString(R.string.love_share));
		}
		
		
		bar.setDisplayHomeAsUpEnabled(true);
		
		initView();
	}


	private void initView() {
		etTitle = (EditText) findViewById(R.id.et_title);
		etContent = (EditText) findViewById(R.id.et_content);
		
		btnPic1 =  (ImageView) findViewById(R.id.img_btn_add_1);
		btnPic2 =  (ImageView) findViewById(R.id.img_btn_add_2);
		btnPic3 =  (ImageView) findViewById(R.id.img_btn_add_3);
		btnPic4 =  (ImageView) findViewById(R.id.img_btn_add_4);
		
		btnPic1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentBtn = 1;
				showAddPicWindow();
			}
		});
		
		btnPic2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentBtn = 2;
				showAddPicWindow();
			}
		});
		
		btnPic3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentBtn = 3;
				showAddPicWindow();
			}
		});
		
		btnPic4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentBtn = 4;
				showAddPicWindow();
			}
		});
		
	}
	
	
	private void showAddPicWindow(){
		//实例化SelectPicPopupWindow  
        menuWindow = new SelectPicPopupWindow(PostGoodORBadORShareActivity.this, itemsOnClick);  
        //显示窗口  
        menuWindow.showAtLocation(findViewById(R.id.et_title), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置  
        menuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent_white));  
	}
	

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在提交...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.submit, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_submit:
			onSubmit();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}

	private void onSubmit() {
		UserCache userCache = UserCache.getInstance();
		
		String picPathArr = "";
		for(String picurl : picUrls){
			picPathArr = picPathArr + picurl +",";
		}
		
		UserInfo userInfo =  userCache.getUserInfo();
		PostGoodOrBadOrShareReq reqData = new PostGoodOrBadOrShareReq();
		
		if(userInfo instanceof Teacher){
			Teacher teacher = (Teacher) userInfo;
			reqData.setSchoolcode(teacher.getSchoolId());
		}
		else{
			reqData.setSchoolcode(userCache.getCurrentClass().getSchoolId());
		}
		
		reqData.setUserId(userInfo.getUserId());
		reqData.setSessionKey(userCache.getSessionKey());
		//判断为哪种版块的编辑
		if(plates.equals(DeedType.good)){
			reqData.setPlates(DeedType.good);
		}else if(plates.equals(DeedType.bad)){
			reqData.setPlates(DeedType.bad);
		}else if(plates.equals(DeedType.lover_share)){
			reqData.setPlates(DeedType.lover_share);
		}
		
		reqData.setReleaseoperid(userInfo.getUserId());
		reqData.setReleaseusername(userInfo.getUserName());
		reqData.setContent(etContent.getText().toString());
		reqData.setTitle(etTitle.getText().toString());
		
		if(picPathArr.length() > 0 ){
			picPathArr = picPathArr.substring(0,picPathArr.length()-1);
			reqData.setType("2");
		}
		else{
			reqData.setType("1");
		}
		
		reqData.setPicPathArr(picPathArr);
		
		showProgressDialog();
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						closeProgressDialog();
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						MessageBox.showServerError(PostGoodORBadORShareActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			BaseResponse respData = new BaseResponse(response);
			if (respData.getAck() == Ack.SUCCESS) {
				MessageBox.showMessage(PostGoodORBadORShareActivity.this, "提交成功！");
				etTitle.setText("");
				etContent.setText("");
				this.finish();
			} else {
				MessageBox.showAckError(PostGoodORBadORShareActivity.this, respData.getAck());
			}
		} catch (Exception e) {
			MessageBox.showParserError(PostGoodORBadORShareActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	
	//为弹出窗口实现监听类  
    private OnClickListener  itemsOnClick = new OnClickListener(){  
        public void onClick(View v) {  
            menuWindow.dismiss();  
            switch (v.getId()) {  
            case R.id.btn_take_photo:  
            	takePhoto();
                break;  
            case R.id.btn_pick_photo:     
            	pickPhoto();
                break;  
            default:  
                break;  
            }  
        }  
          
    };  
    
    
    //拍照
    private void takePhoto(){
    	
		Intent intent = new Intent(PostGoodORBadORShareActivity.this, CramaActivity.class);
		intent.setAction("com.stinfo.pushme.activity.crama");
		intent.putExtra("PHOTO_TYPE", PhotoType.PHOTO_REQUEST_TAKEPHOTO);
		startActivityForResult(intent, PhotoType.PHOTO_REQUEST_TAKEPHOTO);
    }
    
    //从相册取
    private void pickPhoto(){
		Intent intent = new Intent(PostGoodORBadORShareActivity.this, CramaActivity.class);
		intent.putExtra("PHOTO_TYPE", PhotoType.PHOTO_REQUEST_GALLERY);
		intent.setAction("com.stinfo.pushme.activity.crama");
		startActivityForResult(intent, PhotoType.PHOTO_REQUEST_GALLERY);
    	
    }
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
	        case PhotoType.PHOTO_REQUEST_CUT:// 拍照的取得的结果
	            // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null){
                	if(data.getStringExtra("serverPhotoUrl")!=null){
                		Log.d(TAG, "获得到的服务器的图片地址"+data.getStringExtra("serverPhotoUrl"));
                		picUrls.add(data.getStringExtra("serverPhotoUrl"));
                		
                	}
                	if(data.getStringExtra("localPhotoUrl")!=null){
                		Log.d(TAG, "获得到的本地的图片地址"+data.getStringExtra("localPhotoUrl"));
                		Bitmap bm = BitmapFactory.decodeFile(data.getStringExtra("localPhotoUrl")); 
                		if(currentBtn==1){
                			
                			btnPic1.setImageBitmap(bm); 
                			btnPic1.setScaleType(ImageView.ScaleType.FIT_XY); 
                		}
                		else if(currentBtn==2){
                			btnPic2.setImageBitmap(bm); 
                			btnPic2.setScaleType(ImageView.ScaleType.FIT_XY); 
                		}
                		else if(currentBtn==3){
                			btnPic3.setImageBitmap(bm); 
                			btnPic3.setScaleType(ImageView.ScaleType.FIT_XY); 
                		}
                		else if(currentBtn==4){
                			btnPic4.setImageBitmap(bm); 
                			btnPic4.setScaleType(ImageView.ScaleType.FIT_XY); 
                		}
                		else{
                			Log.d(TAG, "哪个按钮？");
                		}
                	}
                }
            break;
	    }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
}
