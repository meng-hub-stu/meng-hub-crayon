package com.crayon.core.controlller;

import com.crayon.common.core.util.R;
import com.crayon.core.service.CoreService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mengdl
 * @date 2025/3/1
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/netty-core")
public class CoreController {

    private final CoreService coreService;

    @GetMapping(value = "/sendMessage")
    @Schema(name = "发送消息", description = "模拟发送消息")
    public R<Void> sendMessage(){
        coreService.sendMessage();
        return R.ok();
    }

}
