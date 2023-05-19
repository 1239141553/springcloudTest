package huawei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huawei.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 用户mapper
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 用户详情
     *
     * @param userName 用户名
     * @return UserInfo
     */
    UserInfo getByUserName(@Param("userName") String userName);

    /**
     * 用户详情
     *
     * @param id 用户ID
     * @return UserInfo
     */
    UserInfo getById(@Param("id") Long id);
}
