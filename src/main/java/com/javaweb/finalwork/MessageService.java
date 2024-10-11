package com.javaweb.finalwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByCreateTimeDesc();
    }

    public Message saveMessage(Message message) {
        message.onCreate();
        return messageRepository.save(message);
    }

    public Optional<Message> findMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public void deleteMessageById(Integer id) {
        messageRepository.deleteById(id);
    }


    public Message updateMessage(Integer id, Message updatedMessage) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message existingMessage = optionalMessage.get();
            existingMessage.setName(updatedMessage.getName());
            existingMessage.setContent(updatedMessage.getContent());
            // 也可以设置更新时间戳等字段
            return messageRepository.save(existingMessage);
        } else {
            throw new RuntimeException("Message with id " + id + " not found");
        }
    }



}
