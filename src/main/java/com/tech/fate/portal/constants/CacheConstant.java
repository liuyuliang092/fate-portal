/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tech.fate.portal.constants;

public interface CacheConstant {

	/**
	 * 字典信息缓存（含禁用的字典项）
	 */
    public static final String SYS_DICT_CACHE = "sys:cache:dict";

	/**
	 * 字典信息缓存 status为有效的
	 */
	public static final String SYS_ENABLE_DICT_CACHE = "sys:cache:dictEnable";
	/**
	 * 表字典信息缓存
	 */
    public static final String SYS_DICT_TABLE_CACHE = "sys:cache:dictTable";
	public static final String SYS_DICT_TABLE_BY_KEYS_CACHE = SYS_DICT_TABLE_CACHE + "ByKeys";

	/**
	 * 数据权限配置缓存
	 */
    public static final String SYS_DATA_PERMISSIONS_CACHE = "sys:cache:permission:datarules";

	/**
	 * 缓存用户信息
	 */
	public static final String SYS_USERS_CACHE = "sys:cache:user";

	/**
	 * 全部部门信息缓存
	 */
	public static final String SYS_DEPARTS_CACHE = "sys:cache:depart:alldata";


	/**
	 * 全部部门ids缓存
	 */
	public static final String SYS_DEPART_IDS_CACHE = "sys:cache:depart:allids";


	/**
	 * 测试缓存key
	 */
	public static final String TEST_DEMO_CACHE = "test:demo";

	/**
	 * 字典信息缓存
	 */
	public static final String SYS_DYNAMICDB_CACHE = "sys:cache:dbconnect:dynamic:";

	/**
	 * gateway路由缓存
	 */
	public static final String GATEWAY_ROUTES = "sys:cache:cloud:gateway_routes";


	/**
	 * gateway路由 reload key
	 */
	public static final String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

	/**
	 * TODO 冗余代码 待删除
	 *插件商城排行榜
	 */
	public static final String PLUGIN_MALL_RANKING = "pluginMall::rankingList";
	/**
	 * TODO 冗余代码 待删除
	 *插件商城排行榜
	 */
	public static final String PLUGIN_MALL_PAGE_LIST = "pluginMall::queryPageList";


	/**
	 * online列表页配置信息缓存key
	 */
	public static final String ONLINE_LIST = "sys:cache:online:list";

	/**
	 * online表单页配置信息缓存key
	 */
	public static final String ONLINE_FORM = "sys:cache:online:form";

	/**
	 * online报表
	 */
	public static final String ONLINE_RP = "sys:cache:online:rp";

	/**
	 * online图表
	 */
	public static final String ONLINE_GRAPH = "sys:cache:online:graph";
	/**
	 * 拖拽页面信息缓存
	 */
	public static final String DRAG_PAGE_CACHE = "drag:cache:page";
}
