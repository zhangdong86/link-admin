package com.link.admin.system.dao;

import java.util.List;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.Dict;

/**
 * 字典接口
* @ClassName: IDictDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 252956 
* @date 2021年1月7日 上午9:38:16 
*
 */
public interface IDictDao {
	JqGridPage<Dict> selectPage(Dict dict);

	List<Dict> select(Dict dict);

	Dict select(Integer id);

	int insert(Dict dict);

	int update(Dict dict);

	int delete(Dict dict);

}
