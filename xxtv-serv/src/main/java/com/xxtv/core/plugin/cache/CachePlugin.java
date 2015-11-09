/*
 * <p>版权所有: 版权所有(C)2015-2020</p>
 * <p>公   司: 淮安微赢互通重庆公司产品研发部</p>
 */
package com.xxtv.core.plugin.cache;

import com.jfinal.plugin.IPlugin;
import com.xxtv.core.kit.CacheKit;

/**
 * <p>文件名称: CachePlugin.java</p>
 * <p>文件描述: 本类描述</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>完成日期：2015年8月26日</p>
 * <p>修改记录0：无</p>
 * @version 1.0
 * @author  曹渊华
 */
public class CachePlugin implements IPlugin
{

	/* (non-Javadoc)
	 * @see com.jfinal.plugin.IPlugin#start()
	 */
	@Override
	public boolean start()
	{
		CacheKit.getInstance().init();
		return true;
	}

	/* (non-Javadoc)
	 * @see com.jfinal.plugin.IPlugin#stop()
	 */
	@Override
	public boolean stop()
	{
		CacheKit.getInstance().destory();
		return true;
	}
}
