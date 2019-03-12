package util;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.elastos.dao.ServiceAccessKeyRepository;
import org.elastos.dto.ServiceAccessKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static org.elastos.constants.Constants.ACCESS_KEY_STATUS_OFF;

/**
 * Created by wanghan on 2017/7/30.
 */
public class AccessKeyUtil {

    private static Logger logger = LoggerFactory.getLogger(AccessKeyUtil.class);

    private static ServiceAccessKeyRepository serviceAccessKeyRepository;

    public static void setServiceAccessKeyRepository(ServiceAccessKeyRepository serviceAccessKeyRepository) {
        AccessKeyUtil.serviceAccessKeyRepository = serviceAccessKeyRepository;
    }

    public static Long access(String accessStr) {
        //accessStr="{id:org.elastos.app.id;time:1547448012;auth:90c0687ea72f1c2b0d5dfcd4c47a86a7}"
        Map<String, String> map = (Map<String, String>) JSON.parse(accessStr);

        String accId = map.get("id");
        Optional<ServiceAccessKey> accessKeyOp = serviceAccessKeyRepository.findByKeyId(accId);
        if (!accessKeyOp.isPresent()) {
            logger.info("access id failed no id. input accessStr:" + accessStr);
            System.out.println("access id failed no id. input accessStr:" + accessStr);
            return null;
        }

        ServiceAccessKey accessKey = accessKeyOp.get();
        if (ACCESS_KEY_STATUS_OFF.equals(accessKey.getStatus())) {
            logger.info("access id failed status off input accessStr:" + accessStr);
            System.out.println("access id failed status off input accessStr:" + accessStr);
            return null;
        }

        String time = String.valueOf(map.get("time"));
        SimpleHash hash = new SimpleHash("md5", accessKey.getKeySecret(), time);
        String encodedPassword = hash.toHex();
        if (!encodedPassword.equals(map.get("auth"))) {
            logger.error("access accSecret wrong.");
            System.out.println("access accSecret wrong.");
            return null;
        }

        return accessKey.getUserServiceId();
    }
}
