package com.link.admin.system.service;

import java.util.List;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.Dict;


/**
 * 字典业务层接口
 * 
 * @ClassName: IDictService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:47:15
 *
 */
public interface IDictService {

	JqGridPage<Dict> queryPage(Dict dict);

	List<Dict> queryByType(String type);

	boolean add(Dict dict);

	boolean update(Dict dict);

	boolean delete(Integer id);

}
