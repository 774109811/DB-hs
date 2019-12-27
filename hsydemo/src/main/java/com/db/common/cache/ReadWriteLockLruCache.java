package com.db.common.cache;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/***
 * 假设此类是一个缓存对象：
 * 此对象如何设计？基于LRU算法
 * 何为LRU算法？最近最少使用算法
 * 缓存
 * 
 */
//这个注解自己定义的 有个这个注解点击用户第一次在数据库取,第二次就在缓存中取
public class ReadWriteLockLruCache<K,V>{
	private int maxSize;
	private LinkedHashMap<K, V> cacheMap;
	//创建一个读写锁
	private final ReentrantReadWriteLock readWriteLock=
			new ReentrantReadWriteLock();
	//获取读锁
	private ReadLock readLock = readWriteLock.readLock();
	//获取读锁
	private WriteLock writeLock = readWriteLock.writeLock();
	public ReadWriteLockLruCache(final int maxSize) {
		this.maxSize = maxSize;
		cacheMap=new LinkedHashMap<K,V>() {
			private static final long serialVersionUID = -1429056094423997184L;
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
				// TODO Auto-generated method stub
				return size()>maxSize;//如果大于3各缓存就删除不常用的
			}
		};
	}
	public V put(K key,V value) {//只能有一个现在执行写操作
		try{
			writeLock.lock();
			return cacheMap.put(key, value);
			}finally{
			writeLock.unlock();
			}
	}
	public  V get(K key){//运行多个线程并发读
		try{
		readLock.lock();
		return cacheMap.get(key);
		}finally{
		readLock.unlock();
		}
	}
	
}
