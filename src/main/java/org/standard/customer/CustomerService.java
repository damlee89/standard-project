package org.standard.customer;

public interface CustomerService {
	CustomerVO getCustomer(CustomerVO vo);
	//�α����� �� �� ������ ������ ���� �޼ҵ�
	void joinCustomer(CustomerVO vo);
	//������ �� �������� �Ѱ��ִ� �޼ҵ�.
}
