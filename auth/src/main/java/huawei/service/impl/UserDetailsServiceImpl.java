package huawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huawei.pojo.LoginUser;
import com.huawei.pojo.UserInfo;
import com.huawei.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author hjf
 * @date 2022-10-24 14:24
 * @describe
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoService userInfoService;

    /**
     * 用户密码登录
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户的接口
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        UserInfo userInfo = userInfoService.getOne(wrapper);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        return getUserDetails(userInfo);
    }

    /**
     * 构建用户信息
     * @param userInfo
     * @return 用户详情
     */
    private LoginUser getUserDetails(UserInfo userInfo) {
        // UserVO是用户实体类，AuthUserDetails是SpringSecurity认证用户详情对象
        LoginUser userDetail = new LoginUser();
        // 1. 用户详情封装（此处由于是继承关系，可以使用属性复制的方式）
        BeanUtils.copyProperties(userInfo, userDetail);
        return userDetail;

    }
}