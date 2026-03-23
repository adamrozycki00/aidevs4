package com.tenetmind.aidevs.domain.ports.out;

public interface ApiCaller {

  Object callApi(String uri, Object requestBody);

  String getApiKey();
}
