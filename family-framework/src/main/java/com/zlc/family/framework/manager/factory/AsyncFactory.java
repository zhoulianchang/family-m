package com.zlc.family.framework.manager.factory;

import com.zlc.family.common.constant.Constants;
import com.zlc.family.common.utils.DateUtils;
import com.zlc.family.common.utils.LogUtils;
import com.zlc.family.common.utils.ServletUtils;
import com.zlc.family.common.utils.StringUtils;
import com.zlc.family.common.utils.ip.AddressUtils;
import com.zlc.family.common.utils.ip.IpUtils;
import com.zlc.family.common.utils.spring.SpringUtils;
import com.zlc.family.manage.service.IAccountService;
import com.zlc.family.system.domain.SysLogininfor;
import com.zlc.family.system.domain.SysOperLog;
import com.zlc.family.system.service.ISysLogininforService;
import com.zlc.family.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author family
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args) {
        DateUtils.setDefaultTimeZone();
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginTime(DateUtils.getNowDate(Constants.TIME_GMT8));
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 通知账户金额
     *
     * @param accountId 账户id
     * @param mobile    手机号
     * @return 任务task
     */
    public static TimerTask notifyAccountAmount(final Long accountId, final BigDecimal amount, final String mobile) {
        return new TimerTask() {
            @Override
            public void run() {
                // 根据id获取当前的account账户
                SpringUtils.getBean(IAccountService.class).notifyAccountAmount(accountId, amount, mobile);
            }
        };
    }

    /**
     * 通知账户金额
     *
     * @param accountId 账户id
     * @param mobile    手机号
     * @return 任务task
     */
    public static TimerTask notifyAccountAmount(final Long deptId) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IAccountService.class).notifyAccountAmount(deptId);
            }
        };
    }
}
