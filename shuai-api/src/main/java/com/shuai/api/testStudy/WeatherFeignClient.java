package com.shuai.api.testStudy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 编写 feign 接口测试向 第三方接口发送请求
 * 指定url,像url去去发送请求，而不走 value指定的
 * 没有指定url,则会去注册中心去查找weather-client服务
 *
 * openfeign 可以调用自己写的，也可以调用别人写的
 * 自己写的叫做业务API
 * 别人写的叫做第三方API
 */
@FeignClient(value = "weather-client", url = "http://aliv18.data.moji.com")
public interface WeatherFeignClient {


    @PostMapping("/whapi/json/alicityweather/condition")
    String getWeather(@RequestHeader("Authorization") String auth,
                    @RequestParam("token") String token,
                    @RequestParam("cityId") String cityId);
}
