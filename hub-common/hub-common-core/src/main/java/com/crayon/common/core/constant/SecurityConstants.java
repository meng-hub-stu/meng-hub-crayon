package com.crayon.common.core.constant;

/**
 * @author hytech
 * @date 2017-12-18
 */
public interface SecurityConstants {

	/**
	 * 启动时是否检查Inner注解安全性
	 */
	boolean INNER_CHECK = true;

	/**
	 * 刷新
	 */
	String REFRESH_TOKEN = "refresh_token";

	/**
	 * 验证码有效期
	 */
	int CODE_TIME = 60;

	/**
	 * 验证码长度
	 */
	String CODE_SIZE = "6";

	/**
	 * 角色前缀
	 */
	String ROLE = "ROLE_";

	/**
	 * 前缀
	 */
	String HY_PREFIX = "hy_";

	/**
	 * token 相关前缀
	 */
	String TOKEN_PREFIX = "token:";

	/**
	 * oauth 相关前缀
	 */
	String OAUTH_PREFIX = "oauth:";

	/**
	 * 授权码模式code key 前缀
	 */
	String OAUTH_CODE_PREFIX = "oauth:code:";

	/**
	 * 项目的license
	 */
	String HY_LICENSE = "https://hytech.com";

	/**
	 * 内部
	 */
	String FROM_IN = "Y";

	/**
	 * 标志
	 */
	String FROM = "from";

	/**
	 * 请求header
	 */
	String HEADER_FROM_IN = FROM + "=" + FROM_IN;

	/**
	 * OAUTH URL
	 */
	String OAUTH_TOKEN_URL = "/oauth2/token";

	/**
	 * 移动端授权
	 */
	String GRANT_MOBILE = "mobile";

	/**
	 * TOC 客户端
	 */
	String HEADER_TOC = "CLIENT-TOC";

	/**
	 * TOC 客户端
	 */
	String HEADER_TOC_YES = "Y";

	/**
	 * User-Agent
	 */
	String USER_AGENT = "User-Agent";
	/**
	 * User-Agent
	 */
	String ACCEPT_LANGUAGE = "Accept-Language";

	/**
	 * {bcrypt} 加密的特征码
	 */
	String BCRYPT = "{bcrypt}";

	/**
	 * 客户端模式
	 */
	String CLIENT_CREDENTIALS = "client_credentials";

	/**
	 * 客户端编号
	 */
	String CLIENT_ID = "client_id";

	/**
	 * 客户端唯一令牌
	 */
	String CLIENT_RECREATE = "recreate_flag";

	/**
	 * 用户ID字段
	 */
	String DETAILS_USER_ID = "user_id";

	/**
	 * 用户名
	 */
	String DETAILS_USERNAME = "username";

	/**
	 * 姓名
	 */
	String NAME = "name";

	/**
	 * 协议字段
	 */
	String DETAILS_LICENSE = "license";

	/**
	 * 激活字段 兼容外围系统接入
	 */
	String ACTIVE = "active";

	/**
	 * AES 加密
	 */
	String AES = "aes";

	/**
	 * 授权码模式confirm
	 */
	String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

	/**
	 * {noop} 加密的特征码
	 */
	String NOOP = "{noop}";

	/**
	 * 短信登录 参数名称
	 */
	String SMS_PARAMETER_NAME = "mobile";

	/**
	 * 手机号登录
	 */
	String APP = "mobile";

	/**
	 * 用户信息
	 */
	String DETAILS_USER = "user_info";

}
