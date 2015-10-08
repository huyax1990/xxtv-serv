package com.xxtv.base.controller;

import java.util.List;

import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.xxtv.base.common.BaseController;
import com.xxtv.base.model.SysLoginLogModel;
import com.xxtv.base.model.SysMenusModel;
import com.xxtv.core.plugin.annotation.Control;

/**
 * 
 * <p>文件名称: MenuController.java</p>
 * <p>文件描述: 菜单管理模块</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>完成日期：2015年8月25日</p>
 * <p>修改记录0：无</p>
 * @version 1.0
 * @author  曾芸杰
 */
@Control(controllerKey = "/menu")
public class MenuController extends BaseController
{

	@SuppressWarnings("unused")
	private static final Logger	LOG		= Logger.getLogger(MenuController.class);

	private static String		control	= "菜单管理模块";

	public void index()
	{
		List<SysMenusModel> menus = SysMenusModel.dao.allMenus();
		setAttr("menusJson", JsonKit.toJson(menus));
		render("base/menu_mgr");
	}

	public void add()
	{
		SysMenusModel menu = getModel(SysMenusModel.class, "sysmenus");
		if (menu.save())
		{
			setAttr(RESULT, true);
			setAttr(MESSAGE, "新增成功！");
			setAttr("id", menu.get("id"));
			SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->添加了名称为" + menu.getStr("menuName") + "的菜单");
		}
		else
		{
			setAttr(RESULT, false);
			setAttr(MESSAGE, "新增失败！");
		}
		renderJson();
	}

	public void update()
	{
		SysMenusModel menu = getModel(SysMenusModel.class, "sysmenus");
		if (menu.get("id") != null && menu.update())
		{
			setAttr(RESULT, true);
			setAttr(MESSAGE, "更新成功！");
			SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->修改了名称为" + menu.getStr("menuName") + "的菜单");
		}
		else
		{
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
		}
		renderJson();
	}

	public void del()
	{
		Integer id = getParaToInt("id");
		if (id != null && SysMenusModel.dao.deleteById(id))
		{
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
			SysLoginLogModel.dao.saveSysLog(getUser(), getRealIpAddr(getRequest()), control + "-->添加了id为" + id + "的菜单");
		}
		else
		{
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}

}
