package com.stinfo.pushme.activity;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stinfo.pushme.R;
import com.stinfo.pushme.common.AppConstant;
import com.stinfo.pushme.common.AppConstant.PhotoType;
import com.stinfo.pushme.common.UserCache;
import com.stinfo.pushme.util.FileUtils;
import com.stinfo.pushme.util.MessageBox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class CramaActivity extends Activity {
    
	private String TAG = CramaActivity.class.getName();
	
    private Button creama=null;
    
    private ImageView img=null;
    
    private TextView text=null;
    
    private String serverPhotoUrl = "";
    private String localPhotoUrl="";
    
    private File tempFile = null;
    
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    
    private Button saveUploadBtn = null;
    private Button cancelBtn = null;
    
	private ProgressDialog prgDialog = null;
	
	public void showProgressDialog() {
		prgDialog = new ProgressDialog(CramaActivity.this);
		prgDialog.setMessage("正在上传图片中...");
		prgDialog.show();
	}

	public void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}
	
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crama);
        init();
    }
 
    private void init() {
        // TODO Auto-generated method stub
        
    saveUploadBtn = (Button) findViewById(R.id.btn_upload_pic);
    
    cancelBtn = (Button) findViewById(R.id.btn_cancel_pic);
    
    saveUploadBtn.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(localPhotoUrl!=null && localPhotoUrl.length() > 0){
				try {
					savePhotoToServer(localPhotoUrl);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	});
    
    
    cancelBtn.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(CramaActivity.this,PostGoodORBadORShareActivity.class);
			intent.setAction("com.stinfo.pushme.activity.deed");
			CramaActivity.this.setResult(PhotoType.PHOTO_REQUEST_CUT, intent);
			CramaActivity.this.finish();
		}
	});
    
    
    img=(ImageView) findViewById(R.id.iv_take_pic);
    
    text=(TextView) findViewById(R.id.tv_take_pic);
        
	    if(getIntent()!=null){
	    	 
	    	
	    	if(getIntent().getIntExtra("PHOTO_TYPE",1)==PhotoType.PHOTO_REQUEST_TAKEPHOTO){
	    		
	    		tempFile = new File(Environment.getExternalStorageDirectory(),
	    	            getPhotoFileName());
	    		
	    		Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            // 指定调用相机拍照后照片的储存路径
	            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(tempFile));
	            startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
	    	}
	    	else if(getIntent().getIntExtra("PHOTO_TYPE",1)==PhotoType.PHOTO_REQUEST_GALLERY){
	    		  Intent intent = new Intent();  
	    	      intent.setAction(Intent.ACTION_PICK);  
	    	      intent.setType("image/*");  
	    	      
	    		 startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	    	}
	    	else{
	    		Log.d(TAG, "没有合适的传入类型");
	    	}
	    }
    
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
            startPhotoZoom(Uri.fromFile(tempFile));
            break;
        case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
            // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
            if (data != null){
                 startPhotoZoom(data.getData());
            }
            break;
        case PHOTO_REQUEST_CUT:// 返回的结果
            if (data != null){
            	 sentPicToNext(data);
            }
               
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
 
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
 
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
 
    // 将进行剪裁后的图片传递到下一个界面上
    private void sentPicToNext(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo==null) {
                img.setImageResource(R.drawable.album_default_photo);
            }else {
                img.setImageBitmap(photo);
                 
                 String temPath =  getPhotoFileName();
                 
                 boolean flag = FileUtils.saveBitmap2file(photo, temPath);
                 
                 if(flag){
                	 localPhotoUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+temPath;
                 }
                 else{
                	 Toast.makeText(CramaActivity.this, "存储卡存储空间不足", Toast.LENGTH_LONG).show();
                 }
                
            }
        }
    }
 
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    
    
    
    
	private void savePhotoToServer(final String photourl) throws FileNotFoundException{
		showProgressDialog();
		if(photourl==null || ("").equals(photourl)){
			  Toast.makeText(CramaActivity.this, "获取不到图片", Toast.LENGTH_LONG).show();
		}
		else{
			RequestParams params = new RequestParams();
			params.put("sessionkey", UserCache.getInstance().getSessionKey());
			params.put("photo", new File(photourl));
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(60000);  
			client.post(AppConstant.School_Platform_Photo_URL , 
					params, new AsyncHttpResponseHandler(){
			    
			    @Override
			    public void onFailure(Throwable error, String content) {
			    	closeProgressDialog();
			        Toast.makeText(CramaActivity.this, "上传失败！", Toast.LENGTH_LONG).show();
			    }
			    
			    @Override
			    public void onSuccess(int statusCode, String content) {
			    	closeProgressDialog();
			    	if(content!=null && content.length() > 0){
			    		try {
							JSONObject rootObj = new JSONObject(content);
							
//							if(rootObj.has("ack")){
//								if(rootObj.getInt("ack")==0){
//									if(rootObj.has("response")){
//										JSONObject response  = rootObj.getJSONObject("response");
//										
//										if(response.has("serverPath")){
//											serverPhotoUrl = serverPhotoUrl + response.getString("serverPath");
//										}
//										if(response.has("filePath")){
//											
//											JSONArray fip = response.getJSONArray("filePath");
//											serverPhotoUrl = serverPhotoUrl + fip.getString(0);
//										}
//										
//										
//										Intent intent = new Intent(CramaActivity.this,PostGoodORBadORShareActivity.class);
//										intent.setAction("com.stinfo.pushme.activity.deed");
//										intent.putExtra("serverPhotoUrl", serverPhotoUrl);
//										intent.putExtra("localPhotoUrl", photourl);
//										CramaActivity.this.setResult(PhotoType.PHOTO_REQUEST_CUT, intent);
//										CramaActivity.this.finish();
//									}
//								}
//								else{
//									MessageBox.showAckError(CramaActivity.this, rootObj.getInt("ack"));
//								}
//								
//							}
							
							
							if(rootObj.has("file_url")){
										serverPhotoUrl = rootObj.getString("file_url");
										Intent intent = new Intent(CramaActivity.this,PostGoodORBadORShareActivity.class);
										intent.setAction("com.stinfo.pushme.activity.deed");
										intent.putExtra("serverPhotoUrl", serverPhotoUrl);
										intent.putExtra("localPhotoUrl", photourl);
										CramaActivity.this.setResult(PhotoType.PHOTO_REQUEST_CUT, intent);
										CramaActivity.this.finish();
								
							}
							else{
								Toast.makeText(CramaActivity.this, "请重传，服务器错误！", Toast.LENGTH_LONG).show();
							}
							
						} catch (JSONException e) {
							closeProgressDialog();
							e.printStackTrace();
						}
			    	}
			    	else
			    	{
			    		Toast.makeText(CramaActivity.this, "请重传", Toast.LENGTH_LONG).show();
			    	}
			    	
			    }
			    
			});
			
		}
		
	}
    
    
    
    
}