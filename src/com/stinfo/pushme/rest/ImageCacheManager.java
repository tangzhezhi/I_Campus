package com.stinfo.pushme.rest;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ImageCacheManager {
	private static ImageCacheManager mInstance;
	private ImageLoader mImageLoader;

	private ImageCacheManager() {
	}

	public static synchronized ImageCacheManager getInstance() {
		if (mInstance == null) {
			mInstance = new ImageCacheManager();
		}

		return mInstance;
	}

	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			RequestQueue requestqueue = RequestController.getInstance().getRequestQueue();
			mImageLoader = new ImageLoader(requestqueue, new BitmapCache());
		}

		return mImageLoader;
	}

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}

			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}
	}
}
