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

import kr.co.model.FaqVO;
import kr.co.service.FaqService;

@Controller
@RequestMapping("/faq/*")
public class FaqController {
	
	// log 메서드 사용
	private static final Logger log = LoggerFactory.getLogger(FaqController.class);
	
	@Autowired
	private FaqService service;

	// FAQ 목록페이지 
	@GetMapping("/faqlist")
	public void faqListGET(Model model) {
		
		log.info("FAQ 목록페이지 진입");
		model.addAttribute("faqlist", service.faqlist());
	}
	
	// FAQ 등록페이지
	@GetMapping("/faqregistr")
	public void faqRegistrGET() {
		
		log.info("FAQ 등록페이지 진입");
	}
	
	// FAQ 게시판 등록
	@PostMapping("/faqregistr")
	public String faqRegistrPOST(FaqVO faqvo, RedirectAttributes rttr) {
		log.info("FaqVO: " + faqvo);
		service.faqregistr(faqvo);
		rttr.addFlashAttribute("result", "faqregistr success");
		return "redirect:/faq/faqlist";
	}
	
	
	
	// FAQ 수정
	// RedirectAttributes -> 수정 후 목록페이지로 이동시 수정된 데이터를 같이 전송하기위한
	@PostMapping("/faqupdate")
	public String faqUpdatePOST(FaqVO faqvo , RedirectAttributes rttr) {
		
		rttr.addFlashAttribute("result", "faqupdate success");
		return "redirect:/faq/faqlist";
	}
	
}
