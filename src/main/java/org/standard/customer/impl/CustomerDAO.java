package org.standard.customer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.standard.customer.CustomerVO;

@Repository("customerDAO")
public class CustomerDAO {
	@Autowired
	//SqlSessionTemplate mySQL; // applicationContext���� �ʿ�.
	
	private String loc = "org.standard.CustomerMapper.";
	
	/*
	public CustomerVO getCustomer(CustomerVO vo) {
		return mySQL.selectOne(loc+"getCustomer", vo);
	}
	*/
	
	public void joinCustomer(CustomerVO vo) {
		//mySQL.insert(loc+"joinCustomer", vo);
		//mySQL ���� �� ���۷� getCustomer�� joinCustomer ���� �ʿ�.
	}
}
