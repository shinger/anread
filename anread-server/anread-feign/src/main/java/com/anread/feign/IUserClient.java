package com.anread.feign;

import com.anread.common.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://127.0.0.1:8001", name = "anread-user-user")
public interface IUserClient {

    @PutMapping("/user/reading-record")
    Result incrementReadingRecord(@RequestParam("userId") String userId, @RequestParam("duration") Integer duration);

}
