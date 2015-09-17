/*******************************************************************************
 *  (C) Copyright 2009 Molisys Solutions Co., Ltd. , All rights reserved       *
 *                                                                             *
 *  This source code and any compilation or derivative thereof is the sole     *
 *  property of Molisys Solutions Co., Ltd. and is provided pursuant to a      *
 *  Software License Agreement.  This code is the proprietary information      *
 *  of Molisys Solutions Co., Ltd and is confidential in nature.  Its use and  *
 *  dissemination by any party other than Molisys Solutions Co., Ltd is        *
 *  strictly limited by the confidential information provisions of the         *
 *  Agreement referenced above.                                                *
 ******************************************************************************/
package vn.edu.hcmut.emrre.core.entity.sentence;

import java.util.List;

import com.molisys.framework.base.BaseService;

/**
 * @author sinhlk
 *
 */
public interface SentenceService extends BaseService<Sentence> {
	List<Sentence> getListSenByRecordId(long recordId, String dataSet);

	/**
	 * @param id
	 */
	void lableForOneRecord(long id);
}
