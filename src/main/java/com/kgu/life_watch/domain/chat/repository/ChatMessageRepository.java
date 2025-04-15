package com.kgu.life_watch.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kgu.life_watch.domain.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long roomId);
}
