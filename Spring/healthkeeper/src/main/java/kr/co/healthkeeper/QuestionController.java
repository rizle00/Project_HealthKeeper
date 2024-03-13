package kr.co.healthkeeper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.model.QsCriteria;
import kr.co.model.QsPageMakeDTO;
import kr.co.model.QsReplyVO;
import kr.co.model.QsVO;
import kr.co.service.QsReplyService;
import kr.co.service.QsService;

@Controller
@RequestMapping("/question/*")
public class QuestionController {
	
	@Autowired
	private QsService service;
	
	@Autowired
	private QsReplyService replyservice;
	
	// log 메서드 사용 => consol창에 더 자세히 볼수있음
	private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
	
	// 질문게시판 목록페이지(페이징 적용)
	@GetMapping("/qslist")
	public void qsListGET(Model model, QsCriteria qcri) {
		
		log.info("질문게시판 페이지 진입");
		model.addAttribute("qslist", service.getlistPaging(qcri));
		
		int total = service.getTotal(qcri);
		QsPageMakeDTO qpageMake = new QsPageMakeDTO(qcri, total);
		model.addAttribute("qpageMake", qpageMake);
	}
	

	// 질문게시판 등록페이지 진입
	@GetMapping("/qsregistr")
	public void qsRegistrGET() {
		log.info("질문게시판 등록 페이지 진입");
	}
	
	// 질문게시판 등록
	@PostMapping("/qsregistr")
	public String qsRegistrPOST(QsVO qs, RedirectAttributes rttr) {
		log.info("QsVO :" + qs);
		service.qsregistr(qs);
		rttr.addFlashAttribute("result", "registr success");
		return "redirect:/question/qslist";
	}
	
	// 질문게시판 조회
	@GetMapping("/qsget")
	public void qsGetPageGET(int QUE_ID, QsVO vo, Model model, QsCriteria qcri) {
		// 조회수 관련 로직
		service.qsViews(QUE_ID);
		
		model.addAttribute("pageInfo", service.getpage(QUE_ID));
		model.addAttribute("qcri", qcri);
		
		List<QsReplyVO> replyList = replyservice.readReply(vo.getQUE_ID());
		model.addAttribute("replyList", replyList);
	}
	
	// 질문게시판 수정페이지 진입
	@GetMapping("/qsupdate")
	public void qsUpdateGET(int QUE_ID, Model model, QsCriteria qcri) {
		
		model.addAttribute("pageInfo", service.getpage(QUE_ID));
		model.addAttribute("qcri", qcri);
	}
	
	// 질문게시판 수정
	@PostMapping("/qsupdate")
	public String qsUpdatePOST(QsVO qs, RedirectAttributes rttr) {
		
		service.update(qs);
		rttr.addFlashAttribute("result", "update success");
		
		return "redirect:/question/qslist"; 
	}
	
	// 질문게시판 삭제
	@PostMapping("/qsdelete")
	public String qsDeletePOST(int QUE_ID, RedirectAttributes rttr) {
		
		service.delete(QUE_ID);
		rttr.addFlashAttribute("result", "qsdelete success");
		
		return "redirect:/question/qslist";
	}
	
	// 질문게시판 댓글작성
	@PostMapping("/replyRegistr")
	public String replyRegistrPOST(QsReplyVO replyvo, QsCriteria qcri, RedirectAttributes rttr) {
		replyservice.registrReply(replyvo);
		
		rttr.addAttribute("QS_BNO", replyvo.getQS_BNO());
		rttr.addAttribute("pageNum", qcri.getPageNum());
		rttr.addAttribute("amount", qcri.getAmount());
		rttr.addAttribute("keyword", qcri.getKeyword());
		rttr.addAttribute("type", qcri.getType());
		
		return "redirect:/question/qsget";
	}
	
	// 질문게시판 댓글수정 페이지 진입
	@GetMapping("/replyupdate")
	public void replyUpdateGET(QsCriteria qcri, Model model,QsReplyVO replyvo){
		
		model.addAttribute("replyupdate", replyservice.selectReply(replyvo.getQRNO()));
		model.addAttribute("qcri", qcri);
		
	}
	
	// 질문게시판 댓글수정
	@PostMapping("/replyupdate")
	public String replyUpdatePOST(QsReplyVO replyvo, QsCriteria qcri, RedirectAttributes rttr){
		
		log.info("reply update");
		
		replyservice.updateReply(replyvo);
		
		rttr.addAttribute("QS_BNO", replyvo.getQS_BNO());
		rttr.addAttribute("QRNO", replyvo.getQRNO());
		rttr.addAttribute("pageNum", qcri.getPageNum());
		rttr.addAttribute("amount", qcri.getAmount());
		rttr.addAttribute("keyword", qcri.getKeyword());
		rttr.addAttribute("type", qcri.getType());
		
		return "redirect:/question/qsget";
		
	}
	
	// 질문게시판 댓글삭제 페이지 진입
	@GetMapping("/replydelete")
	public void replyDeleteGET(QsCriteria qcri, Model model, int QUE_ID){
			
		model.addAttribute("replydelete", replyservice.selectReply(QUE_ID));
		model.addAttribute("qcri", qcri);
			
	}
	
	
	// 질문게시판 댓글삭제
	@PostMapping("/replydelete")
	public String replyDeletePOST(@RequestParam int QRNO, QsReplyVO replyvo, QsCriteria qcri, RedirectAttributes rttr) {
		
		replyservice.deleteReply(QRNO);
		
		rttr.addAttribute("QS_BNO", replyvo.getQS_BNO());
		rttr.addAttribute("QRNO", replyvo.getQRNO());
		rttr.addAttribute("pageNum", qcri.getPageNum());
		rttr.addAttribute("amount", qcri.getAmount());
		rttr.addAttribute("keyword", qcri.getKeyword());
		rttr.addAttribute("type", qcri.getType());
		
		return "redirect:/question/qsget";
	}
}
