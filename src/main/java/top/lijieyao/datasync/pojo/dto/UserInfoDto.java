package top.lijieyao.datasync.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 用户信息Dto
 * @Author: LiJieYao
 * @Date: 2022/4/25 10:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String userName;

    private String password;

    private String sex;

    private String age;

    private Integer skipSwitch = 1;
}
