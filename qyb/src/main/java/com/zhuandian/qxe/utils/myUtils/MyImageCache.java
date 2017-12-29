package com.zhuandian.qxe.utils.myUtils;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片缓存类
 * Created by 谢栋 on 2016/12/4.
 */
@Deprecated
public class MyImageCache implements ImageCache {

	private static MyImageCache myImageCache=null;
	public static MyImageCache getMyImageCache(){
		if(myImageCache==null){
			myImageCache=new MyImageCache();
		}
		return myImageCache;
	}
	private MyImageCache(){
		
	}
	
	
	private MyLruCache myLruCache = new MyLruCache((int) (Runtime.getRuntime()
			.maxMemory() / 8));
	private Map<String, SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();

    // 它是loader获取bitmap的方法
    // 如果返回null，volley就去磁盘上自己去找，如果没找到，他自己又去网络上找�����������
	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		bitmap = myLruCache.get(url);
		if (bitmap != null) {
			return bitmap;
		} else {
			SoftReference<Bitmap> softbitmap = map.get(url);
			if (softbitmap != null) {
				bitmap = softbitmap.get();
				if (bitmap != null) {
					myLruCache.put(url, bitmap);
					return bitmap;
				}
			}
		}
		return null;
	}

    // volley从磁盘或者网络上寻找到了bitmap就会回调这个方法���
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		myLruCache.put(url, bitmap);
	}

	class MyLruCache extends LruCache<String, Bitmap> {

		public MyLruCache(int maxSize) {
			super(maxSize);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected int sizeOf(String key, Bitmap value) {
			// TODO Auto-generated method stub
			return value.getByteCount();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {

			super.entryRemoved(evicted, key, oldValue, newValue);
			// TODO Auto-generated method stub
			if (evicted) {
				SoftReference<Bitmap> bitmap = new SoftReference<Bitmap>(
						oldValue);
				map.put(key, bitmap);
			}
		}

	}

}
