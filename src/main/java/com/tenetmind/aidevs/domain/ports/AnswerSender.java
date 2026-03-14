package com.tenetmind.aidevs.domain.ports;

public interface AnswerSender {

  Object send(String task, Object answer);
}
