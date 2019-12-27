package com.db.common.cache;

import java.util.LinkedHashMap;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/***
 * 假设此类是一个缓存对象：
 * 此对象如何设计？基于LRU算法
 * 何为LRU算法？最近最少使用算法
 */

public class SynchronizedLruCache<K,V> extends LinkedHashMap<K, V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3206343045052587427L;
		
	private int maxSize;
	
	public SynchronizedLruCache(int maxSize) {
		super(maxSize,0.75f,true);
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size()>maxSize;
	}
	
	@Override
	public synchronized V put(K key, V value) {
		// TODO Auto-generated method stub
		return super.put(key, value);
	}
	@Override
	public synchronized V get(Object key) {
		// TODO Auto-generated method stub
		return super.get(key);
	}
}
