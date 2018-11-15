package com.apollo.produce;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :zoboy
 * @Description:
 * @ Date: Created in 2018-11-14 15:03
 */
@RestController
public class ProduceController {

    // activemq://queue
    @Value("${apollo.queue}")
    private String queue;
    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping("/send/{jsonStr}")
    public void send(@PathVariable String jsonStr){
        producerTemplate.sendBody(queue,jsonStr);
    }
}
