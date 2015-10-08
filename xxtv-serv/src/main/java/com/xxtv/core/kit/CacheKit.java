/*
 * <p>版权所有: 版权所有(C)2015-2020</p>
 * <p>公   司: 淮安微赢互通重庆公司产品研发部</p>
 */
package com.xxtv.core.kit;

import com.jfinal.plugin.redis.Redis;
import com.xxtv.tools.RedisConstants;

/**
 * <p>文件名称: CacheKit.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>完成日期：2015年8月26日</p>
 * <p>修改记录0：无</p>
 * @version 1.0
 * @author  曹渊华
 */
public class CacheKit
{

	private static CacheKit cacheKit = null;

	private CacheKit()
	{

	}

	public static CacheKit getInstance()
	{
		if (cacheKit == null)
		{
			synchronized (CacheKit.class)
			{
				if (cacheKit == null)
				{
					cacheKit = new CacheKit();
				}
			}
		}
		return cacheKit;
	}

	public void init()
	{
		
	}

	public void destory()
	{
		Redis.use().del(RedisConstants.HASH_ITGWALL_SECRET_KEY);
	}
}
