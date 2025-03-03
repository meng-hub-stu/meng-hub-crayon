package com.crayon.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * yes and no enum
 *
 * @author hytech
 * @date 2023/10/25
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum {

	YES("1", "是"), NO("0", "否");

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 描述
	 */
	private String desc;

}
