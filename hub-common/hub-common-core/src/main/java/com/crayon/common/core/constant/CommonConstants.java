package com.crayon.common.core.constant;

/**
 * @author hytech
 * @date 2017/10/29
 */
public interface CommonConstants {

	/**
	 * header 中租户ID
	 */
	String TENANT_ID = "TENANT-ID";
	/**
	 * header 中切权限租户ID
	 */
	String TENANT_ID_AUTH = "TENANT-ID-AUTH";

	/**
	 * header 中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * 租户ID
	 */
	Long DEFAULT_TENANT_ID = 0L;

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单树根节点
	 */
	Long MENU_TREE_ROOT_ID = -1L;

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "hy-ui";

	/**
	 * 移动端工程名
	 */
	String UNI_END_PROJECT = "hy-app";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "hy";

	/**
	 * 公共参数
	 */
	String HY_PUBLIC_PARAM_KEY = "HY_PUBLIC_PARAM_KEY";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 200;

	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * 默认存储bucket
	 */
	String BUCKET_NAME = "hytech";

	/**
	 * 滑块验证码
	 */
	String IMAGE_CODE_TYPE = "blockPuzzle";

	/**
	 * 验证码开关
	 */
	String CAPTCHA_FLAG = "captcha_flag";

	/**
	 * 密码传输是否加密
	 */
	String ENC_FLAG = "enc_flag";

	/**
	 * 客户端允许同时在线数量
	 */
	String ONLINE_QUANTITY = "online_quantity";

	/**
	 * 请求开始时间
	 */
	String REQUEST_START_TIME = "REQUEST-START-TIME";

	/**
	 * 当前页
	 */
	String CURRENT = "current";

	/**
	 * size
	 */
	String SIZE = "size";

}
