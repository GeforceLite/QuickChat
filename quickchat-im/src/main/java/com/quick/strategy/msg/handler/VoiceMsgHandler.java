package com.quick.strategy.msg.handler;

import cn.hutool.json.JSONUtil;
import com.quick.adapter.ChatMsgAdapter;
import com.quick.enums.ChatMsgEnum;
import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import com.quick.pojo.dto.ChatMsgDTO;
import com.quick.pojo.dto.FileExtraDTO;
import com.quick.pojo.po.QuickChatMsg;
import com.quick.store.QuickChatMsgStore;
import com.quick.strategy.file.handler.VoiceHandler;
import com.quick.strategy.msg.AbstractChatMsgStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  17:05
 * @Description: 语音消息
 * @Version: 1.0
 */
@Component
public class VoiceMsgHandler extends AbstractChatMsgStrategy {
    @Autowired
    private QuickChatMsgStore msgStore;
    @Autowired
    private VoiceHandler voiceHandler;
    @Value("${quick-chat.voice-time-limit}")
    private Integer voiceTimeLimit;
    @Value("${quick-chat.size.voice}")
    private Integer voiceSize;

    @Override
    protected ChatMsgEnum getEnum() {
        return ChatMsgEnum.VOICE;
    }

    @Override
    public QuickChatMsg sendMsg(ChatMsgDTO msgDTO) throws Throwable {
        // 校验语音时长、语音文件大小
        Boolean checkVoiceTime = msgDTO.getExtraInfo().getVoiceTime() > voiceTimeLimit;
        Boolean checkVoiceSize = (msgDTO.getExtraInfo().getSize() / 1024 / 1024) > voiceSize;
        if (checkVoiceTime || checkVoiceSize) {
            voiceHandler.deleteFile(msgDTO.getContent());
            ResponseEnum voiceTimeEnum = ResponseEnum.VOICE_TIME_NOT_ALLOW;
            voiceTimeEnum.setMsg(String.format(voiceTimeEnum.getMsg(), voiceTimeLimit));
            throw new QuickException(voiceTimeEnum);
        }

        // 保存聊天信息
        String fromId = msgDTO.getFromId();
        String toId = msgDTO.getToId();
        String url = msgDTO.getContent();
        FileExtraDTO extraInfo = msgDTO.getExtraInfo();
        QuickChatMsg chatMsg = ChatMsgAdapter.buildChatMsgPO(fromId, toId,
                url, JSONUtil.toJsonStr(extraInfo), this.getEnum().getCode());
        msgStore.saveMsg(chatMsg);
        return chatMsg;
    }
}
