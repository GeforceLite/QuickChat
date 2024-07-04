package com.quick.controller;

import com.quick.enums.ResponseEnum;
import com.quick.pojo.vo.ChatSessionVO;
import com.quick.response.R;
import com.quick.service.QuickChatSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 徐志斌
 * @Date: 2023/11/19 11:11
 * @Version 1.0
 * @Description: 聊天会话
 */
@Api(tags = "聊天会话")
@RestController
@RequestMapping("/chat/session")
public class ChatSessionController {
    @Autowired
    private QuickChatSessionService sessionService;

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public R getSessionList() {
        List<ChatSessionVO> result = sessionService.getSessionList();
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("恢复会话（激活状态）")
    @PostMapping("/active")
    public R active(String toId) {
        sessionService.activeSession(toId);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("查询会话详情")
    @GetMapping("/getSessionInfo")
    public R getSessionInfo(String fromId, String toId) {
        ChatSessionVO result = sessionService.getSessionInfo(fromId, toId);
        return R.out(ResponseEnum.SUCCESS, result);
    }

    @ApiOperation("修改已读时间")
    @PostMapping("/updateReadTime")
    public R updateSession(Long sessionId) {
        sessionService.updateLastReadTime(sessionId);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("删除会话")
    @DeleteMapping("/delete")
    public R deleteSession(Long sessionId) {
        sessionService.deleteSession(sessionId);
        return R.out(ResponseEnum.SUCCESS);
    }

    @ApiOperation("置顶会话")
    @DeleteMapping("/top")
    public R topSession(Long sessionId) {
        sessionService.topSession(sessionId);
        return R.out(ResponseEnum.SUCCESS);
    }


    /**
     * TODO 消息免打扰
     */
}
