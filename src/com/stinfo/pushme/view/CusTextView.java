package com.stinfo.pushme.view;

import com.stinfo.pushme.R;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;


public class CusTextView extends TextView {
	private Context mContext;
	private PopupWindow mPopupWindow; 

	public CusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		this.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					setTextColor(android.graphics.Color.BLUE); //选定为蓝色
					getPopupWindowsInstance();
					mPopupWindow.showAsDropDown(CusTextView.this,getWidth()/2-mPopupWindow.getWidth()/2,0);	
					return true;
				}
			});
	}

	
    /**
     * 销毁 PopupWindow
     */
    private void dismissPopupWindowInstance(){
    	  if (null != mPopupWindow) { 
              mPopupWindow.dismiss(); 
    	  }
    }
    /**
     * 获得 PopupWindow 实例
     */
    private void getPopupWindowsInstance(){
    	if(mPopupWindow!=null){
    		mPopupWindow.dismiss();
    	}else{
    		initPopuptWindow();
    	}
    }
	/*
     * 创建PopupWindow
     */ 
    private void initPopuptWindow() { 
        LayoutInflater layoutInflater = LayoutInflater.from(mContext); 
        View popupWindow = layoutInflater.inflate(R.layout.textview_popup_window, null); 
        Button btnCopy = (Button) popupWindow.findViewById(R.id.textViewbtnCopy); 
        btnCopy.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View v) {
				setTextColor(android.graphics.Color.BLACK);  //初始化为黑色
				dismissPopupWindowInstance();
				ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("content",getText().toString());
				clipboard.setPrimaryClip(clip);
				//clipboard.setText(getText());
			}
		});
        
        //给 PopupWindow一个 固定的宽度
		mPopupWindow = new PopupWindow(popupWindow, dipTopx(mContext, 50),ViewGroup.LayoutParams.WRAP_CONTENT);
		// 这行代码 很重要
		mPopupWindow.setBackgroundDrawable(getDrawable());
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				setTextColor(android.graphics.Color.BLACK); //取消选定为黑色
			}
		});
    } 
    /**
     * 生成一个 透明的背景图片
     * @return
     */
    private Drawable getDrawable(){
    	ShapeDrawable bgdrawable =new ShapeDrawable(new OvalShape());
    	bgdrawable.getPaint().setColor(getResources().getColor(android.R.color.transparent));
    	return   bgdrawable;
    }
    /**
 	 * 转换成对应的px值
 	 * @param context
 	 * @param dipValue
 	 * @return
 	 */
 	public static int dipTopx(Context context, float dipValue){  
 		 final float scale = context.getResources().getDisplayMetrics().density;  
         return (int)(dipValue * scale + 0.5f);  
     }  
}
