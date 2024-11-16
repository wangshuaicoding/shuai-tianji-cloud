package com.shuai.auth.util;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.shuai.common.constants.AuthErrorInfo;
import com.shuai.common.constants.JwtConstants;
import com.shuai.common.domain.dto.LoginUserDTO;
import com.shuai.common.exceptions.BadRequestException;
import com.shuai.common.utils.AssertUtils;
import com.shuai.common.utils.BooleanUtils;
import com.shuai.common.utils.StringUtils;
import com.shuai.common.utils.UserContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

import static com.shuai.common.constants.JwtConstants.JWT_REFRESH_TTL;
import static com.shuai.common.constants.JwtConstants.JWT_TOKEN_TTL;


@Component
public class JwtTool {
    private final StringRedisTemplate stringRedisTemplate;
    private final JWTSigner jwtSigner;

    public JwtTool(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        // 随机生成密钥对，此处用户可自行读取`KeyPair`、公钥或私钥生成`JWTSigner`
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm("rs256")));
    }

    /**
     * 创建 access-token
     * Header 头部信息，主要声明了JWT的签名算法等信息
     * Payload 载荷信息，主要承载了各种声明并传递明文数据
     * Signature 签名，拥有该部分的JWT被称为JWS，也就是签了名的JWT，用于校验数据
     */
    public String createToken(LoginUserDTO userDTO) {
        // 1.生成jws
        return JWT.create()
                .setPayload(JwtConstants.PAYLOAD_USER_KEY, userDTO)
                .setExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_TTL.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 创建刷新token，并将token的JTI记录到Redis中
     *
     * @param userDetail 用户信息
     * @return 刷新token
     */
    public String createRefreshToken(LoginUserDTO userDetail) {
        // 1.生成 JTI（JTI是指JWT令牌标识符，即 JSON Web Token (JSON网页令牌) 的标识符。）
        String jti = UUID.randomUUID().toString(true);
        // 2.生成jwt
        // 2.1.如果是记住我，则有效期7天，否则30分钟
        Duration ttl = BooleanUtils.isTrue(userDetail.getRememberMe()) ? JwtConstants.JWT_REMEMBER_ME_TTL : JWT_REFRESH_TTL;
        // 2.2.生成token
        /**
         * jti 声明：
         * jti（JWT ID）是一个唯一的标识符，用于标识每个 JWT 令牌。
         * 它可以帮助系统跟踪和验证 JWT 是否已经被使用过，从而提高安全性。
         */
        String token = JWT.create()
                .setJWTId(jti)
                .setPayload(JwtConstants.PAYLOAD_USER_KEY, userDetail)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
        // 3.缓存jti，有效期与token一致，过期或删除JTI后，对应的refresh-token失效
        stringRedisTemplate.opsForValue()
                .set(JwtConstants.JWT_REDIS_KEY_PREFIX + userDetail.getUserId(), jti, ttl);
        return token;
    }

    /**
     * 解析刷新token
     *
     * @param refreshToken 刷新token
     * @return 解析刷新token得到的用户信息
     */
    public LoginUserDTO parseRefreshToken(String refreshToken) {
        // 1.校验token是否为空
        // AssertUtils.isNotNull(refreshToken, AuthErrorInfo.Msg.INVALID_TOKEN);
        if (StringUtils.isEmpty(refreshToken)) {
            throw new BadRequestException(AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        // 2.校验并解析jwt
        JWT jwt;
        try {
            // 解析并验证签名算法 2024/9/15 15:11 By 少帅
            jwt = JWT.of(refreshToken).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN, e);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new BadRequestException(400, AuthErrorInfo.Msg.EXPIRED_TOKEN);
        }
        // 4.数据格式校验
        Object userPayload = jwt.getPayload(JwtConstants.PAYLOAD_USER_KEY);
        Object jtiPayload = jwt.getPayload(JwtConstants.PAYLOAD_JTI_KEY);
        if (jtiPayload == null || userPayload == null) {
            // 数据为空
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }

        // 5.数据解析
        LoginUserDTO userDTO;
        try {
            userDTO = ((JSONObject) userPayload).toBean(LoginUserDTO.class);
        } catch (RuntimeException e) {
            // 数据格式有误
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }

        // 6.JTI校验
        String jti = stringRedisTemplate.opsForValue().get(JwtConstants.JWT_REDIS_KEY_PREFIX + userDTO.getUserId());
        if (!StringUtils.equals(jti, jtiPayload.toString())) {
            // jti不一致
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        return userDTO;
    }

    /**
     * 清理刷新refresh-token的jti，本质是refresh-token作废
     */
    public void cleanJtiCache() {
        stringRedisTemplate.delete(JwtConstants.JWT_REDIS_KEY_PREFIX + UserContext.getUser());
    }
}
