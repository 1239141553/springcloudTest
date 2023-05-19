package huawei.config;

import com.alibaba.fastjson.JSON;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    // private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        // 给/oauth/token接口加属性roles,author
        String s = JSON.toJSONString(authentication.getPrincipal());
        List<Object> authorities = JSON.parseArray(s);
        StringBuilder stringBuilder = new StringBuilder();
        for (Object authority : authorities) {
            Map map = (Map) authority;
            stringBuilder.append(map.get("authority"));
            stringBuilder.append(",");
        }
        String roles = stringBuilder.toString();
        additionalInfo.put("roles", roles.substring(0, roles.length() - 1));
        additionalInfo.put("author", "sophia");
        // additionalInfo.put("createTime", df.format(LocalDateTime.now()));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
