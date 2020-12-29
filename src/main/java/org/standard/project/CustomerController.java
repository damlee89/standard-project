package org.standard.project;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.standard.project.common.Encrypt;
import org.standard.project.customer.CustomerService;
import org.standard.project.customer.CustomerVO;

@Controller
@RequestMapping(value = "/Customer")
public class CustomerController {
	@Resource(name = "CustomerService")
	CustomerService customerService;

	@RequestMapping(value = "/login_form", method = RequestMethod.GET)
	public void login() {
	}

	@RequestMapping(value = "/login_ok", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView login_ok(CustomerVO vo, ModelAndView mav, HttpSession session,  HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		// 아이디 없을 시(d
		if (vo.getC_Id() == null || vo.getC_Id().equals("")) {
			out.println("<script>alert('아이디를 입력해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/login_form");
			return mav;
		}
		// 비밀번호 없을시
		else if (vo.getC_Password() == null || vo.getC_Password().equals("")) {
			out.println("<script>alert('비밀번호를 입력해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/login_form");
			return mav;
		}
		
		System.out.println(">>> 로그인 프로세스 입장");
		System.out.println(vo);
		// 암호화처리.(사용자가 입력한 비밀번호를 암호화처리하여 vo객체에 setPassword()처리후에 디비조회.
		vo.setC_Password(Encrypt.encrypt(vo.getC_Password()));

		CustomerVO customer = customerService.getCustomer(vo);
		//등록되지 않은 아이디로 로그인하려는 경우
		//가입승인처리 되지않은 판매자고객이 로그인하려는경우
				if(customer==null) {
					out.println("<script>alert('등록되지 않은 아이디입니다.'); history.go(-1);</script>");
					out.flush();
					mav.setViewName("Customer/login_form");
					return mav;
				}
		System.out.println(vo);
		System.out.println(customer);
		System.out.println(vo.getC_Password());
		System.out.println(customer.getC_Password());
		
		
		try {
			if (vo.getC_Id().equals(customer.getC_Id()) && vo.getC_Password().equals(customer.getC_Password())) {
				System.out.println("로그인 되어씁니다");
				customer.setC_Password(null);
				session.setAttribute("loginCustomer", customer);
				mav.setViewName("index");
			} else {
				out.println("<script>alert('아이디 또는 비밀번호가 틀립니다.'); history.go(-1);</script>");
				out.flush();
				mav.setViewName("Customer/login_form");
				return mav;
			}
		} catch (NullPointerException e) {
			out.println("<script>alert('아이디 또는 비밀번호가 틀립니다.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/login_form");
			return mav;
		}
		return mav;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/Customer/login_form";
	}

	// Register. Get일땐 페이지로 단순이동, Post면 등록처리
	@RequestMapping(value = "/Register", method = RequestMethod.GET)
	public String register_form() {
		return "Customer/Register";
	}

	@RequestMapping(value = "/Register", method = RequestMethod.POST)
	public ModelAndView register(CustomerVO vo, ModelAndView mav, HttpServletResponse response, HttpServletRequest req) throws IOException {
		System.out.println("가입기능");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		vo.setC_Password(Encrypt.encrypt(vo.getC_Password()));
		System.out.println((String) req.getParameter("c_Password"));
		System.out.println((String) req.getParameter("c_Password_confirm"));
		System.out.println("vo값"+vo);
		if ((String) req.getParameter("c_Password") == "") {
			out.println("<script>alert('비밀번호를 입력해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/Register");
			return mav;
		} else if (req.getParameter("c_Password").equals(req.getParameter("c_Password_confirm")) != true) {
			out.println("<script>alert('비밀번호와 비밀번호 확인을 같게해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/Register");
			return mav;
		}
		
		String phoneNum = req.getParameter("mobile1-1") + req.getParameter("mobile1-2") + req.getParameter("mobile1-3");
		vo.setC_Phone1(phoneNum);
		String phoneNum2 = req.getParameter("mobile2-1") + req.getParameter("mobile2-2")
				+ req.getParameter("mobile2-3");
		vo.setC_Phone2(phoneNum2);
		String emailAddr = req.getParameter("email1") + "@" + req.getParameter("email2");
		vo.setC_Email(emailAddr);
		
		vo.setRole((String)req.getParameter("radiocheck"));
		System.out.println("vo값"+vo);
		if (vo.getRole().equals("개인회원")) {
			customerService.joinCustomer(vo);
		}
		// 셀러면 가입대기목록에 저장(waitingcustomer)
		else if (vo.getRole().equals("기업회원")) {
			customerService.joinWaitingList(vo);
		}
		out.println("<script>alert('가입완료');</script>");
		out.flush();
		mav.setViewName("index");
		// 등록 완료후에는 메인페이지로 이동
		return mav;
	}

	@RequestMapping(value = "/check_Id")
	public void check_Id(HttpServletRequest req, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		// System.out.println(req.getParameter("data"));
		System.out.println(req.getParameter("param"));
		String RequestedID = req.getParameter("param");
		CustomerVO vo = new CustomerVO();
		vo.setC_Id(RequestedID);
		CustomerVO customer = customerService.getCustomer(vo);
		CustomerVO waitingCustomer = customerService.getWaitingCustomer(vo);
		if (customer == null && waitingCustomer == null) {
			// 등록이 되었지 않은 경우
			out.print("사용할 수 있는 아이디 입니다.");
		} else {
			// 등록이 되어있는경우
			out.print("이미 사용중인 아이디입니다.");
		}

	}

	@RequestMapping(value = "/Modify", method = RequestMethod.GET)
	public String modify_form() {
		return "Customer/Modify";
	}

	@RequestMapping(value = "/Modify", method = RequestMethod.POST)
	public ModelAndView modify(CustomerVO vo, ModelAndView mav, HttpServletRequest req, HttpServletResponse response)
			throws IOException {
		System.out.println("수정기능");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		vo.setC_Password(Encrypt.encrypt(vo.getC_Password()));
		System.out.println((String) req.getParameter("c_Password"));
		System.out.println((String) req.getParameter("c_Password_confirm"));
		if ((String) req.getParameter("c_Password") == "") {
			out.println("<script>alert('비밀번호를 입력해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/Modify");
			return mav;
		} else if (req.getParameter("c_Password").equals(req.getParameter("c_Password_confirm")) != true) {
			out.println("<script>alert('비밀번호와 비밀번호 확인을 같게해주세요.'); history.go(-1);</script>");
			out.flush();
			mav.setViewName("Customer/Modify");
			return mav;
		}
		System.out.println(vo);
		String phoneNum = req.getParameter("mobile1-1") + req.getParameter("mobile1-2") + req.getParameter("mobile1-3");
		vo.setC_Phone1(phoneNum);
		String phoneNum2 = req.getParameter("mobile2-1") + req.getParameter("mobile2-2")
				+ req.getParameter("mobile2-3");
		vo.setC_Phone2(phoneNum2);
		String emailAddr = req.getParameter("email1") + "@" + req.getParameter("email2");
		vo.setC_Email(emailAddr);
		customerService.modifyCustomer(vo);
		System.out.println(vo);

		out.println("<script>alert('수정완료.'); history.go(-1);</script>");
		out.flush();
		mav.setViewName("Customer/Modify");
		return mav;

	}

	@RequestMapping(value = "/Delete", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView delete(CustomerVO vo, ModelAndView mav, HttpSession session, HttpServletResponse response)
			throws IOException {
		vo = (CustomerVO) session.getAttribute("loginCustomer");
		System.out.println("삭제기능");
		System.out.println(vo);
		customerService.deleteCustomer(vo);

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script>alert('삭제완료.'); location.href='/project';</script>");
		out.flush();
		mav.setViewName("index");
		return mav;

	}

	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String myCart(HttpSession session) {
		if (session.getAttribute("loginCustomer") == null) {
			return "redirect:/Customer/login_form";
		} else {
			return "Customer/cart";
		}
	}

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String myOrder(HttpSession session) {
		if (session.getAttribute("loginCustomer") == null) {
			return "redirect:/Customer/login_form";
		} else {
			return "Customer/order";
		}

	}

	@RequestMapping(value = "/myPage", method = RequestMethod.GET)
	public String myPageMain(HttpSession session) {
		if (session.getAttribute("loginCustomer") == null) {
			return "redirect:/Customer/login_form";
		} else {
			return "Customer/Mypage";
		}

	}

}