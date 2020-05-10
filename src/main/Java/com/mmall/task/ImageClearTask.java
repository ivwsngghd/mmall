package com.mmall.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImageClearTask {
    @Scheduled(cron = "0 */1 * * * ?")
    public void delImageTask() {

    }

}
