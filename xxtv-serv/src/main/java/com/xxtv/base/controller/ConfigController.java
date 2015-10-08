package com.xxtv.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.redis.Redis;
import com.xxtv.base.common.BaseController;
import com.xxtv.base.common.Pager;
import com.xxtv.base.model.SysConfigModel;
import com.xxtv.base.model.SysLoginLogModel;
import com.xxtv.core.plugin.annotation.Control;
import com.xxtv.tools.EhcacheConstants;
import com.xxtv.tools.RedisConstants;

/**
 * 
 * <p>文件名称: ConfigController.java</p>
 * <p>文件描述: 数据字典模块</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>完成日期：2015年8月25日</p>
 * <p>修改记录0：无</p>
 * @version 1.0
 * @author  曾芸杰
 */
@Control(controllerKey = "config")
public class ConfigController extends BaseController
{
	private static String	control	= "数据字典模块";

	public void index()
	{
		render("base/sys_config");
	}

	/**
	 * 数据字典列表数据
	 */
	public void list()
	{
		Pager pager = createPager();
		pager.addParam("key", getPara("key"));
		pager.addParam("state", getPara("state"));

		Page<SysConfigModel> page = SysConfigModel.dao.pageList(pager);
		setAttr("rows", page.getList());
		setAttr("total", page.getTotalRow());
		SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->查询");
		renderJson();
	}

	/**
	 * 添加
	 */
	public void add()
	{
		Map<String, Object> result = getResultMap();
		SysConfigModel config = getModel(SysConfigModel.class, "sysconfig");
		try
		{
			config.save();
			// 清后台缓存
			CacheKit.removeAll(EhcacheConstants.SYS_CONFIG);
			//缓存数据字典开始
			String key = config.getStr("key");
			List<SysConfigModel> sysConfigModelList = SysConfigModel.dao.find(
					"select `key`,field,`val`,remark from sys_config where state=1 and `key`=? order by sort", key);
			Redis.use().hset(RedisConstants.SYS_DICTKEY, key, sysConfigModelList);
			if ("DRAWAL_FAVORABLE".equals(key))
			{
				Redis.use().hdel(RedisConstants.STATIC_DATASOURCE, RedisConstants.DRAWAL_FAVORABLE_HTML);
			}
			//缓存数据字典开始结束

			result.put(RESULT, true);
			result.put(MESSAGE, "添加字典成功！");
			SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->添加了key为" + config.getStr("key") + "的数据");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.put(RESULT, false);
			result.put(MESSAGE, "添加字典失败！");
		}
		renderJson(result);
	}

	/**
	 * 修改
	 */
	public void update()
	{
		Map<String, Object> result = getResultMap();
		SysConfigModel config = getModel(SysConfigModel.class, "sysconfig");
		try
		{
			config.update();
			result.put(RESULT, true);
			CacheKit.remove(EhcacheConstants.SYS_CONFIG, "KEY_" + config.get("key"));
			//缓存数据字典开始
			String key = config.getStr("key");
			List<SysConfigModel> sysConfigModelList = SysConfigModel.dao.find(
					"select `key`,field,`val`,remark from sys_config where state=1 and `key`=? order by sort", key);
			Redis.use().hset(RedisConstants.SYS_DICTKEY, key, sysConfigModelList);
			if ("DRAWAL_FAVORABLE".equals(key))
			{
				Redis.use().hdel(RedisConstants.STATIC_DATASOURCE, RedisConstants.DRAWAL_FAVORABLE_HTML);
			}
			//缓存数据字典开始结束

			result.put(MESSAGE, "修改字典成功！");
			SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->修改了id为" + config.getInt("id") + "的数据");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.put(RESULT, false);
			result.put(MESSAGE, "修改字典失败！");
		}
		renderJson(result);
	}

	/**
	 * 删除
	 */
	public void batchDel()
	{
		Map<String, Object> result = getResultMap();
		try
		{
			String ids = getPara("ids");
			SysConfigModel.dao.batchDel(ids);
			CacheKit.removeAll(EhcacheConstants.SYS_CONFIG);

			//缓存数据字典开始
			if (ids != null)
			{
				String[] idsArr = ids.split("\\|");
				if (idsArr.length > 0)
				{
					StringBuilder sb = new StringBuilder("select distinct `keys` from sys_config where `key` in (");
					for (int i = 0; i < idsArr.length; i++)
					{
						sb.append("?,");
					}
					sb.setCharAt(sb.length() - 1, ')');
					Map<String, List<SysConfigModel>> sys_dictMap = new HashMap<String, List<SysConfigModel>>();
					List<SysConfigModel> sys_dicts = SysConfigModel.dao.find(sb.toString(), (Object[]) idsArr);
					if (sys_dicts != null)
					{
						for (SysConfigModel sysConfigModel : sys_dicts)
						{
							String key = sysConfigModel.getStr("key");
							if (sys_dictMap.containsKey(key))
							{
								sys_dictMap.get(key).add(sysConfigModel);
							}
							else
							{
								List<SysConfigModel> sys_dict_itm = new ArrayList<SysConfigModel>();
								sys_dict_itm.add(sysConfigModel);
								sys_dictMap.put(key, sys_dict_itm);
							}
						}
					}
					Set<String> keys = sys_dictMap.keySet();
					Iterator<String> keyIterator = keys.iterator();
					while (keyIterator.hasNext())
					{
						String key = keyIterator.next();
						Redis.use().hset(RedisConstants.SYS_DICTKEY, key, sys_dictMap.get(key));
					}
				}
			}
			//缓存数据字典开始结束

			result.put(RESULT, true);
			result.put(MESSAGE, "版本删除成功！");
			SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->删除了id为" + getPara("ids") + "的数据");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.put(RESULT, false);
			result.put(MESSAGE, "版本删除失败！");
		}
		renderJson(result);
	}
}
