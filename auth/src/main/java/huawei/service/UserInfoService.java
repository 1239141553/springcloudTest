package huawei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.pojo.Result;
import com.huawei.pojo.UserInfo;


/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 用户service
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户登录
     *
     * @return Result<UserInfo>
     */
    Result<UserInfo> login(String userName, String password);

    /**
     * 用户详情
     *
     * @param id
     * @return Result<UserInfo>
     */
    Result<UserInfo> getUserInfoById(Long id);
}
