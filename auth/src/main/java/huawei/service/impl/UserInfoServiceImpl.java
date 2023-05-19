package huawei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.mapper.UserInfoMapper;
import com.huawei.pojo.Result;
import com.huawei.pojo.UserInfo;
import com.huawei.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 用户service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    private final static Integer YES =1;
    /**
     * 用户登录
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfo> login(String userName, String password) {
        UserInfo userInfo = userInfoMapper.getByUserName(userName);
        if(userInfo != null){
            if(userInfo.getDeleted().equals(YES)){
                return Result.fail("001","登录失败，账号已注销");
            }
            if(userInfo.getStatus().equals(YES)){
                return Result.fail("002","登录失败，账号已禁用，请联系客服人员");
            }
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(password, userInfo.getPassword());
            if (!matches) {
                return Result.fail("000","账号或密码错误");
            }
            return Result.ok(userInfo);
        }else{
            return Result.fail("000","账号或密码错误");
        }
    }

    /**
     * 获取用户详情
     *
     * @return Result<UserInfo>
     */
    @Override
    public Result<UserInfo> getUserInfoById(Long id) {
        UserInfo userInfo = userInfoMapper.getById(id);
        if(userInfo != null){
            return Result.ok(userInfo);
        }
        return Result.fail();
    }
}
