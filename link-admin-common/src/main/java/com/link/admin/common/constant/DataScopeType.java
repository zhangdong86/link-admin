package com.link.admin.common.constant;
/**
 * // 数据范围（1：全部数据权限 2：自定数据权限 3：本部门及以下数据权限4：本部门数据权限 5：本人）
* @ClassName: DataScope 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 252956
* @date 2019年11月26日 上午9:59:45 
*
 */
public class DataScopeType {
	
	public static final Integer all=1; //全部数据权限
	
	public static final Integer customize=2;//自定数据权限 
	
	public static final Integer deptAndBelow=3;//本部门及以下数据权限
	
	public static final Integer dept=4;//本部门数据权限
	
	public static final Integer self=5;//本人
	

}
