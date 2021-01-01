package org.standard.project;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.standard.project.brand.BrandDBService;
import org.standard.project.brand.BrandDBVO;
import org.standard.project.common.DeleteUtil;
import org.standard.project.common.UploadUtil;
import org.standard.project.common.UploadUtilProductLong;
import org.standard.project.common.UploadUtilProductThumb;
import org.standard.project.customer.CustomerService;
import org.standard.project.customer.CustomerVO;
import org.standard.project.magazine.MagazineVO;
import org.standard.project.order.OrderHistoryVO;
import org.standard.project.product.ProductChildService;
import org.standard.project.product.ProductChildVO;
import org.standard.project.product.ProductParentService;
import org.standard.project.product.ProductParentVO;

@Controller
@RequestMapping(value = "/Seller")
public class SellerController {
	@Resource(name = "ProductParentService")
	ProductParentService productParentService;
	@Resource(name = "CustomerService")
	CustomerService customerService;
	@Resource(name = "BrandDBService")
	BrandDBService brandDBService;
	@Resource(name = "ProductChildService")
	ProductChildService productChildService;
	@Resource(name = "uploadPath")
	private String uploadPath;
	
	@RequestMapping(value = "/ProductManage", method = RequestMethod.GET)
	public ModelAndView productManage(HttpSession session, ModelAndView mav, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		session.removeAttribute("productParentList");
		System.out.println(session.getAttribute("productParentList"));
		System.out.println("productParentList 보기");
		//로그인후에 세션에 저장된 사용자 객체를 활용.
		CustomerVO customerVO = (CustomerVO)session.getAttribute("loginCustomer");
		if(customerVO == null) {
			out.println("<script>alert('로그인해주세요.');</script>");
			out.flush();
			mav.setViewName("Customer/login_form");
			return mav;
		}else if (customerVO.getBrandName() ==null) {
			out.println("<script>alert('기업회원으로 가입해주세요.');</script>");
			out.flush();
			mav.setViewName("Customer/login_form");
			return mav;
		}
		
		BrandDBVO loginBrand = brandDBService.getBrandId(customerVO);
//		System.out.println(loginBrand);
		
		ArrayList<ProductParentVO> listProductParent = new ArrayList<ProductParentVO>();
		listProductParent = productParentService.listProductParent(loginBrand);
		System.out.println(listProductParent);
		
		mav = new ModelAndView ("/Seller/ProductManage");
		mav.addObject("list", listProductParent);
		return mav;
	}
	
	
	
	@RequestMapping(value = "/ProductAddParent", method = RequestMethod.GET)
	public ModelAndView AddParent(Map<String, Object> map, CustomerVO vo) {
		System.out.println("부모 상품 추가");
		ModelAndView mav = new ModelAndView ("/Seller/ProductAddParent");
		return mav;
	}
	
	@RequestMapping(value = "/ProductAddParent", method = RequestMethod.POST)
	public String magazineWriteAction(HttpSession session, HttpServletRequest req, MultipartHttpServletRequest mhsq, HttpServletResponse response) throws Exception {
		System.out.println("상품 입력 액션");
		ProductParentVO vo = new ProductParentVO();

		String pp_Name = req.getParameter("pp_Name");
		
//		이것을 어떻게 가져올까,, 아이디 값 가져와서 0001만들어주고 
		CustomerVO customerVO = (CustomerVO)session.getAttribute("loginCustomer");
		BrandDBVO loginBrand = brandDBService.getBrandId(customerVO);
		System.out.println(loginBrand);
		
		int int_first_parent_p_Id = loginBrand.getBrandId();
		String string_first_parent_p_Id = String.format("%04d", int_first_parent_p_Id);
		int int_second_parent_p_Id = productParentService.cntBrandProductParent(loginBrand);
		System.out.println(int_second_parent_p_Id);
		String string_second_parent_p_Id = String.format("%04d", int_second_parent_p_Id+1);
		String parent_p_Id = string_first_parent_p_Id.concat(string_second_parent_p_Id);
		
		
		String pp_Category1 = req.getParameter("pp_Category1");
		String pp_Category2 = req.getParameter("pp_Category2");
		vo.setPp_Name(pp_Name);
		vo.setParent_p_Id(parent_p_Id);;
		vo.setPp_Category1(pp_Category1);;
		vo.setPp_Category2(pp_Category2);;
		int pp_Price = Integer.parseInt(req.getParameter("pp_Price"));
		
		vo.setPp_Brand(loginBrand.getBrandId());
		vo.setPp_Price(pp_Price);
		System.out.println(vo);
		
		String imgUploadPath = uploadPath+ File.separator + "productImage" ;
		String fileName = null;
		List<MultipartFile> mf = mhsq.getFiles("m_Img");
		
		if (mf.size() == 1 && mf.get(0).getOriginalFilename().equals("")) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('꼭 이미지를 넣어주세요'); history.go(-1);</script>");
			out.flush();
        } else {
        	String ymdPath = UploadUtilProductThumb.calcPath(imgUploadPath);
            for (int i = 0; i < mf.size(); i++) {
                if(i==0) {
        			fileName = UploadUtilProductThumb.fileUpload(imgUploadPath, mf.get(i).getOriginalFilename(), mf.get(i).getBytes(), ymdPath);
        			vo.setPp_thumb(File.separator + "productImage" + ymdPath + File.separator + fileName);
                }else if(i==1) {
        			fileName = UploadUtilProductLong.fileUpload(imgUploadPath, mf.get(i).getOriginalFilename(), mf.get(i).getBytes(), ymdPath);
        			vo.setPp_image(File.separator + "productImage" + ymdPath +File.separator + fileName);
                }
            }
        }
		
		System.out.println(vo);
		
		productParentService.registProductParent(vo);

		return "redirect:/Seller/ProductManage";
	}
	
	@RequestMapping(value = "/DeleteParentProduct", method = RequestMethod.POST)
	public String deleteParentProduct(@RequestParam(value = "chBox[]") String[] parent_p_Id) throws Exception {
		// 체크한 상품 ID마다 반복해서 사용자 삭제
		System.out.println("선택한 상품 삭제 가동");
		for (String del_Id : parent_p_Id) {
			System.out.println("사용자 삭제 = " + del_Id);
			productParentService.deleteParentProduct(del_Id);
		}
		return "redirect:/Seller/ProductManage";
	}
	
	@RequestMapping(value = "/ModifyParentProduct", method = RequestMethod.GET)
	public ModelAndView selectParentProduct(HttpServletRequest req) {
		System.out.println("선택한 부모상품 수정 가동");
		String parent_p_Id = req.getParameter("seq");
		ProductParentVO vo = new ProductParentVO();
		vo = productParentService.selectParentProduct(parent_p_Id);
		System.out.println(vo);
		ModelAndView mav = new ModelAndView("/Seller/ProductModify");
		mav.addObject("vo",vo);		
		return mav;
	}
	
	@RequestMapping(value = "/ModifyParentProduct", method = RequestMethod.POST)
	public String modifyParentProduct (HttpServletRequest req, MultipartHttpServletRequest images) throws IOException, Exception {
		ProductParentVO vo = new ProductParentVO();
		
		List<MultipartFile> mf = images.getFiles("m_Img");
		if(mf.get(0).getOriginalFilename() == "" && mf.get(1).getOriginalFilename() == "") {
		//따로 이미지 수정을 하지 않았을 때 text만
			System.out.println("이미지 변경 없는 수정");
			vo.setParent_p_Id(req.getParameter("parent_p_Id"));
			vo.setPp_Name(req.getParameter("pp_Name"));
			vo.setPp_Category1(req.getParameter("pp_Category1"));
			vo.setPp_Category2(req.getParameter("pp_Category2"));
			vo.setPp_Price(Integer.parseInt(req.getParameter("pp_Price")));
			System.out.println(vo);
			productParentService.modifyParentProductWithoutImage(vo);
		} else {
			//이미지 수정을 했을 때
			System.out.println("이미지 두개 수정");
			vo.setParent_p_Id(req.getParameter("parent_p_Id"));
			vo.setPp_Name(req.getParameter("pp_Name"));
			vo.setPp_Category1(req.getParameter("pp_Category1"));
			vo.setPp_Category2(req.getParameter("pp_Category2"));
			vo.setPp_Price(Integer.parseInt(req.getParameter("pp_Price")));
			
			if((mf.get(0).getOriginalFilename() != "" && mf.get(1).getOriginalFilename() != "")) {
				String originalThumbPath = req.getParameter("pp_thumb");
				String originalImagePath = req.getParameter("pp_image");
				System.out.println(originalThumbPath);
				System.out.println(originalImagePath);
				//오리지널 폴더에 있던 사진 삭제
				DeleteUtil.deleteImg(uploadPath + originalThumbPath);
				DeleteUtil.deleteImg(uploadPath + originalImagePath);
				System.out.println("pp_thumb"+ originalThumbPath);
				System.out.println("pp_thumb"+ originalImagePath);
				//multipartFile로 받은 이미지 경로 set
				String imgUploadPath = uploadPath + File.separator + "productImage";
				String fileName = null;
				//req.getParameter("m_Img") != null
				
				
		        String ymdPath = UploadUtilProductThumb.calcPath(imgUploadPath);
		        for (int i = 0; i < mf.size(); i++) {
		            if(i==0) {
		        		fileName = UploadUtilProductThumb.fileUpload(imgUploadPath, mf.get(i).getOriginalFilename(), mf.get(i).getBytes(), ymdPath);
		        		vo.setPp_thumb(File.separator + "productImage" + ymdPath + File.separator + fileName);
		            }else if(i==1) {
		        		fileName = UploadUtilProductLong.fileUpload(imgUploadPath, mf.get(i).getOriginalFilename(), mf.get(i).getBytes(), ymdPath);
		        		vo.setPp_image(File.separator + "productImage" + ymdPath + File.separator + fileName);
		            }
		        }
			} else if((mf.get(0).getOriginalFilename() != "" && mf.get(1).getOriginalFilename() == "")) {
				String originalThumbPath = req.getParameter("pp_thumb");
				vo.setPp_image(req.getParameter("pp_image"));
				//오리지널 폴더에 있던 사진 삭제
				DeleteUtil.deleteImg(uploadPath + File.separator + originalThumbPath);
				System.out.println(uploadPath + File.separator + originalThumbPath);
				
				String imgUploadPath = uploadPath + File.separator + "productImage";
				String fileName = null;
		        String ymdPath = UploadUtilProductThumb.calcPath(imgUploadPath);
		        fileName = UploadUtilProductThumb.fileUpload(imgUploadPath, mf.get(0).getOriginalFilename(), mf.get(0).getBytes(), ymdPath);
		        vo.setPp_thumb(File.separator + "productImage" + ymdPath + File.separator + fileName);
		            
			} else if((mf.get(0).getOriginalFilename() == "" && mf.get(1).getOriginalFilename() != "")) {
				vo.setPp_thumb(req.getParameter("pp_thumb"));
				String originalImagePath = req.getParameter("pp_image");
				//오리지널 폴더에 있던 사진 삭제
				DeleteUtil.deleteImg(uploadPath + File.separator + originalImagePath);
				System.out.println(uploadPath + File.separator + originalImagePath);
				
				String imgUploadPath = uploadPath + File.separator + "productImage";
				String fileName = null;
		        String ymdPath = UploadUtilProductThumb.calcPath(imgUploadPath);
		        fileName = UploadUtilProductLong.fileUpload(imgUploadPath, mf.get(1).getOriginalFilename(), mf.get(1).getBytes(), ymdPath);
    			vo.setPp_image(File.separator + "productImage" + ymdPath +File.separator +  fileName);
		            
			} else {
				System.out.println("이미지 문제");
			}
			System.out.println(vo);
			productParentService.modifyParentProductWithImage(vo);
		}
		return "redirect:/Seller/ProductManage";
	}
	
	@RequestMapping(value = "/BuyList", method = RequestMethod.GET)
	public ModelAndView deliveryManage(HttpSession session, ModelAndView mav, HttpServletResponse response) throws IOException {
		mav = new ModelAndView ("/Seller/BuyList");
		return mav;
	}

	@RequestMapping(value = "/Review", method = RequestMethod.GET)
	public ModelAndView sellerReview(HttpSession session, ModelAndView mav, HttpServletResponse response) throws IOException {
		
		mav = new ModelAndView ("/Seller/Review");
		return mav;
	}
	
	@RequestMapping(value = "/ProductAddChild", method = RequestMethod.GET)
	public ModelAndView ProductAddChild(HttpSession session, HttpServletRequest req, ModelAndView mav, HttpServletResponse response) throws IOException {
		System.out.println("선택한 상품의 옵션나열");
		String parent_p_Id = req.getParameter("seq");
		
		ArrayList<ProductChildVO> listProductParent = new ArrayList<ProductChildVO>();
		listProductParent = productChildService.listProductChild(parent_p_Id);
		System.out.println(listProductParent);
		
		mav = new ModelAndView ("/Seller/ProductAddChild");
		mav.addObject("list", listProductParent);
		return mav;
	}
}
