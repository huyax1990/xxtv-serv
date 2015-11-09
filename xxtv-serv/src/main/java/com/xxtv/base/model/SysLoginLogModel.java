package com.xxtv.base.model;

import java.util.Date;

import com.jfinal.plugin.activerecord.Model;
import com.xxtv.core.plugin.annotation.Table;

/**
 * CREATE TABLE `sys_login_log` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `username` varchar(50) NOT NULL,
 * `loginIP` varchar(50) NOT NULL,
 * `loginDate` datetime NOT NULL,
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=579 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC
 */
@SuppressWarnings("serial")
@Table(tableName = "sys_login_log")
public class SysLoginLogModel extends Model<SysLoginLogModel>
{

	public static final SysLoginLogModel	dao	= new SysLoginLogModel();

	public void saveSysLog(SysUserModel user, String ip, String details)
	{
		new SysLoginLogModel().set("userID", user.get("id")).set("username", user.get("username")).set("loginIP", ip).set("loginDate", new Date())
				.save();
	}

}
