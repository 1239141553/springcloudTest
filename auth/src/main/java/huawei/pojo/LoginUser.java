package huawei.pojo;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hjf
 * @date 2022-10-24 14:27
 * @describe
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser extends UserInfo implements UserDetails {

    @ApiModelProperty(value = "凭证")
    private List<GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status != 1;
    }



    private String roles;
    //** 获取角色信息
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StringUtils.isNotBlank(this.roles)){
            //获取数据库中角色
            this.authorities = Stream.of(this.roles.split(",")).map(role ->{
                return new SimpleGrantedAuthority(role);
            }).collect(Collectors.toList());
        }else{
            // 如果角色为空
            this.authorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER");
        }
        return this.authorities;
    }
}

