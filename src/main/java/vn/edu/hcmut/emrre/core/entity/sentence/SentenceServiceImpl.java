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

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.hcmut.emr.utils.SessionHelper;
import org.hcmut.emr.word.Word;
import org.hcmut.emr.word.WordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.molisys.framework.base.BaseServiceImpl;

/**
 * @author sinhlk
 *
 */
public class SentenceServiceImpl extends BaseServiceImpl<Sentence> implements
		SentenceService {
	@Autowired
	private SentenceDao sentenceDao;

	@Autowired
	private WordDao wordDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hcmut.emr.sentence.SentenceService#lableForOneRecord(long)
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void lableForOneRecord(long recordId) {
		System.out.println("SentenceServiceImpl - Lable For One");
		try {
			List<Sentence> sentenses = sentenceDao.search(recordId,
					"record.id", "=", 0, 1000000000);
			System.out.println("Total sentences " + sentenses.size());
			List<NameValuePair> headers = SessionHelper.getListSession();

			String current = "none";
			for (Sentence sentence : sentenses) {
				List<Word> words = wordDao.search(sentence.getId(),
						"sentence.id", "=", 0, 1000);

				current = isHeader(sentence.getContent(), headers, current);
				for (Word word : words) {
					word.setSessionTag(current);
					wordDao.update(word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param content
	 * @param headers
	 * @return
	 */
	private String isHeader(String content, List<NameValuePair> headers,
			String current) {
		for (NameValuePair header : headers) {
			if (content.toLowerCase().equals(header.getName())) {
				return header.getValue();
			}
		}
		return current;
	}

	/**
	 * @return the sentenceDao
	 */
	public SentenceDao getSentenceDao() {
		return sentenceDao;
	}

	/**
	 * @param sentenceDao
	 *            the sentenceDao to set
	 */
	public void setSentenceDao(SentenceDao sentenceDao) {
		this.sentenceDao = sentenceDao;
		this.baseDao = sentenceDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sentence> getListSenByRecordId(long recordId, String dataSet) {
		return sentenceDao.getListSenByRecordId(recordId, dataSet);
	}

}
