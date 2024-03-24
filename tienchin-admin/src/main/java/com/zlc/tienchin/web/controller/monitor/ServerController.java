package com.zlc.tienchin.web.controller.monitor;

import com.zlc.tienchin.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zlc.tienchin.common.core.domain.AjaxResult;

/**
 * 服务器监控
 *
 * @author tienchin
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    @PreAuthorize("hasPermission('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}
