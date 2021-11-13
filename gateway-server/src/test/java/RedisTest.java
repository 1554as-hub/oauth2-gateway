import com.test.GatewayApplication;
import com.test.domain.Redisconstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SpringBootTest(classes = GatewayApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {


    @Autowired
    private RedisTemplate<String , Object> redisTemplate;

    @Test
    public void test1(){

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(Redisconstant.RESOURCE_ROLES_MAP.getValue());
        System.out.println(entries.get("/api-website/echo/**") instanceof List);
        System.out.println(entries);
    }

    @Test
    public void test2() {
        String URI = "http://localhost:7171/api-website/user/currentUser";
        String regx = "/api-website/user/currentUser";

        System.out.println(regx.matches(URI));
    }

}
