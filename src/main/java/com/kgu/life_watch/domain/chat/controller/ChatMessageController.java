package com.kgu.life_watch.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.kgu.life_watch.domain.chat.dto.ChatMessageDto;
import com.kgu.life_watch.domain.chat.dto.response.ChatMessageResponse;
import com.kgu.life_watch.domain.chat.entity.ChatMessage;
import com.kgu.life_watch.domain.chat.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/message")
  public void sendMessage(@Valid ChatMessageDto request, SimpMessageHeaderAccessor accessor) {
    String userId = (String) accessor.getSessionAttributes().get("senderUserId");
    // 실시간으로 방에서 채팅하기
    ChatMessage newChatMessage = chatMessageService.createChatMessage(request, userId);
    log.info("received message: {}", request);

    // 방에 있는 모든 사용자에게 메시지 전송
    messagingTemplate.convertAndSend(
        "/sub/channel/" + request.roomId(), ChatMessageResponse.fromEntity(newChatMessage));
  }
}
