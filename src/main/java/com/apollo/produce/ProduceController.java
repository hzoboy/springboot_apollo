package com.apollo.produce;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.apache.camel.Message;
/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-14 15:03
 */
@RestController
public class ProduceController {

    @Value("${apollo.queue.name}")
    private String queue;
    @Value("${apollo.topic.name}")
    private String topic;
    @Value("${apollo.direct.name}")
    private String direct;

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping("/send/{jsonStr}")
    public void sendqueue(@PathVariable String jsonStr){
        producerTemplate.sendBody(queue,jsonStr);
    }

    @GetMapping("/sendtopic/{jsonStr}")
    public void sendtopic(@PathVariable String jsonStr){
        producerTemplate.sendBody(topic,jsonStr);
    }

    @GetMapping("/sendtopic1/{jsonStr}")
    public void sendtopic1(@PathVariable String jsonStr){
        producerTemplate.setDefaultEndpointUri(direct);
        producerTemplate.sendBody(jsonStr);
    }
}
