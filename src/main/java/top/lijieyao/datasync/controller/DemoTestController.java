package top.lijieyao.datasync.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Demo测试控制器
 * @Author: LiJieYao
 * @Date: 2022/4/25 10:27
 */
@RestController
@RequestMapping("/demo-test")
public class DemoTestController {

    // @PostMapping("/insert")
    // public Map<String, String> insert(@RequestBody UserInfoDto dto) {
    //     Map<String, String> hashMap = Maps.newHashMap();
    //     if (dto.getSkipSwitch() == 1) {
    //         int dbIndex = dto.getUserName().hashCode() % 15;
    //         RedisUtil.switchDatabase(dbIndex);
    //         hashMap.put("dbIndex", String.valueOf(dbIndex));
    //     }
    //
    //     RedisUtil.setObj(dto.getUserName(), dto);
    //     hashMap.put("msg", "success");
    //     return hashMap;
    // }

}
