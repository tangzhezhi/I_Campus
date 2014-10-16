package com.stinfo.pushme.rest;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.stinfo.pushme.util.StringDealTool;

/**
 * 自定义的字符串请求
 * @author lenovo
 *
 */
public class MyStringRequest extends Request<String> {
	private static final String TAG = "MyStringRequest";
	private static final String CONTENT_ENCODING = "UTF-8";
	private static final int TIMEOUT_MS = 30000;

	private final Listener<String> mListener;
	private Map<String, String> mParams;

	public MyStringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		mParams = null;
	}

	public MyStringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
		this(Method.GET, url, listener, errorListener);
	}

	public MyStringRequest(String path, Map<String, String> params, Listener<String> listener,
			ErrorListener errorListener) {
		this(Method.POST, path, listener, errorListener);
		mParams = params;
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//		RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT_MS, 2,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}

	@Override
	protected void deliverResponse(String response) {
		String parsed;
		try {
			
			response = StringDealTool.serverPostStringFilter(response);
			
			parsed = URLDecoder.decode(response, CONTENT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			parsed = response;
		}
		
		mListener.onResponse(parsed);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	public String getUrl() {
		String url = super.getUrl();

		try {
			URL parsedUrl = new URL(url);

			StringBuilder urlBuilder = new StringBuilder();

			urlBuilder.append(parsedUrl.getProtocol());
			urlBuilder.append("://");
			urlBuilder.append(parsedUrl.getAuthority());
			urlBuilder.append(parsedUrl.getPath());
			if (!TextUtils.isEmpty(parsedUrl.getQuery())) {
				urlBuilder.append("?");
				urlBuilder.append(encodeParameters(parsedUrl.getQuery()));
			}

			return urlBuilder.toString();
		} catch (Exception e) {
			Log.e(TAG, "Failed to getUrl: " + e.toString());
			return url;
		}
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams;
	}

	private String encodeParameters(String query) throws UnsupportedEncodingException {
		StringBuilder encodedParams = new StringBuilder();

		int start = 0;
		do {
			int next = query.indexOf('&', start);
			int end = (next == -1) ? query.length() : next;

			int separator = query.indexOf('=', start);
			if (separator > end || separator == -1) {
				separator = end;
			}

			String name = query.substring(start, separator);
			String value = "";
			if (separator < (end - 1)) {
				value = query.substring(separator + 1, end);
			}

			encodedParams.append(URLEncoder.encode(name, CONTENT_ENCODING));
			encodedParams.append('=');
			encodedParams.append(URLEncoder.encode(value, CONTENT_ENCODING));
			if (end < query.length()) {
				encodedParams.append('&');
			}

			start = end + 1;
		} while (start < query.length());

		return encodedParams.toString();
	}
	
	
}