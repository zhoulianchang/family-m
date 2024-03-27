package com.zlc.family.manage.service;

import com.zlc.family.manage.domain.Favor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2024-03-27
 */
public interface IFavorService extends IService<Favor> {

    boolean saveFavor(Favor favor);

    boolean updateFavor(Favor favor);
}
