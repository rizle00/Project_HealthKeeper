package kr.co.healthkeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.model.FaqCriteria;
import kr.co.model.FaqPageMakeDTO;
import kr.co.model.FaqVO;
import kr.co.service.FaqService;

@Controller
@RequestMapping("/faq/*")
public class FaqController {
	
	// log 메서드 사용
	private static final Logger log = LoggerFactory.getLogger(FaqController.class);
	
	@Autowired
	private FaqService service;

	// FAQ 목록페이지 ( 페이징 처리 )
	@GetMapping("/faqlist")
	public void faqListGET(Model model, FaqCriteria fcri) {
		
		log.info("FAQ 목록페이지 진입");
		model.addAttribute("faqlist", service.faqlistPaging(fcri));
		
		int total = service.faqTotal();
        FaqPageMakeDTO fpageMake = new FaqPageMakeDTO(fcri, total);
        model.addAttribute("fpageMake",fpageMake);
	}
	
	// FAQ 등록페이지
	@GetMapping("/faqregistr")
	public void faqRegistrGET() {
		
		log.info("FAQ 등록페이지 진입");
	}
	
	// FAQ 게시판 등록
	@PostMapping("/faqregistr")
	public String faqRegistrPOST(FaqVO faq, RedirectAttributes rttr) {
		log.info("FaqVO: " + faq);
		service.faqregistr(faq);
		rttr.addFlashAttribute("result", "faqregistr success");
		return "redirect:/faq/faqlist";
	}
	
	// FAQ 게시판 조회
	@GetMapping("/faqget")
	public void faqgetGET(String FAQ_ID, Model model, FaqCriteria fcri) {
		model.addAttribute("pageInfo", service.faqpage(FAQ_ID));
		model.addAttribute("fcri", fcri);
	}
	
	
	// FAQ 수정페이지 이동
	@GetMapping("/faqupdate")
	public void faqupdateGET(String FAQ_ID, Model model, FaqCriteria fcri) {
		model.addAttribute("pageInfo", service.faqpage(FAQ_ID));
		model.addAttribute("fcri",fcri);
	}
	
	// FAQ 수정
	@PostMapping("/faqupdate")
	public String faqupdatePOST(FaqVO faq,RedirectAttributes rttr) {
		
		service.faqupdate(faq);
		rttr.addFlashAttribute("result", "faqupdate success");
		return "redirect:/faq/faqlist";
	}
	
	// FAQ 공지사항 삭제
	@PostMapping("/faqdelete")
	public String faqdeletePOST(String FAQ_ID, RedirectAttributes rttr) {
		
		service.faqdelete(FAQ_ID);
		
		rttr.addFlashAttribute("result", "faqdelete success");
		
		return "redirect:/faq/faqlist";
	}
	
}
