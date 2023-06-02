package huawei.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lkx
 * @date 2022-10-24 15:19
 * @describe 登录认证
 */

@Api(tags = "权限控制")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    /**
     * 返回增强
     */
    private final TokenEndpoint tokenEndpoint;

    private final TokenStore tokenStore;


    @PostMapping("/login")
    public Map<String, Object> postAccessToken(Principal principal, @RequestParam Map<String,String> param) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.postAccessToken(principal, param).getBody());
    }

    private Map<String,Object> custom(OAuth2AccessToken oAuth2AccessToken){
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        Map<String,Object> data =  new LinkedHashMap(token.getAdditionalInformation());
        data.put("accessToken",token.getValue());
        data.put("expireIn", token.getExpiresIn());
        data.put("scopes", token.getScope());
        if (token.getRefreshToken() != null){
            data.put("refreshToken",token.getRefreshToken().getValue());
        }
        return data;
    }


    /**
     * 移除access_token和refresh_token，退出登录
     *
     * @param access_token 登录token
     */
    @DeleteMapping(value = "/removeToken", params = "access_token")
    public void removeToken(Principal principal, String access_token) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
        if (accessToken != null) {
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);

            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
        }
    }
}
