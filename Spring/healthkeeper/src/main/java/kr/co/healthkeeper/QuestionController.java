package kr.co.healthkeeper;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


import kr.co.and.firebase.FirebaseCloudMessageService;
import kr.co.and.firebase.RequestDTO;
import kr.co.and.firebase.TypeVO;
import kr.co.model.*;
import kr.co.service.AndMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.service.QsAnswerService;
import kr.co.service.QsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/question/*")
public class QuestionController {

    @Autowired
    private QsService service;

    @Autowired
    private QsAnswerService anservice;
    @Autowired
    AndMemberService andService;
    private FirebaseCloudMessageService firebaseCloudMessageService;

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
    public void qsRegistrGET(Model model) throws Exception {
        log.info("질문게시판 등록 페이지 진입");

        ObjectMapper objm = new ObjectMapper();
        List list = service.catelist();

        String catelist = objm.writeValueAsString(list);
        model.addAttribute("catelist", list);

    }

    // 질문게시판 등록
    @PostMapping("/qsregistr")
    public String qsRegistrPOST(QsVO qs, RedirectAttributes rttr, Model model,
                                MultipartHttpServletRequest qsRequet) throws Exception {
        log.info("QsVO :" + qs);
        service.qsregistr(qs, qsRequet);
        rttr.addFlashAttribute("result", "registr success");


        ObjectMapper objm = new ObjectMapper();
        List list = service.catelist();
        String catelist = objm.writeValueAsString(list);
        model.addAttribute("catelist", catelist);

        return "redirect:/question/qslist";
    }

    // 질문게시판 조회
    @GetMapping("/qsget")
    public void qsGetPageGET(String QUE_ID, QsVO vo, Model model, QsCriteria qcri) throws Exception {

        // 조회수 관련 로직
        service.qsViews(QUE_ID);

        vo = service.getpage(QUE_ID);
        vo.setCategory(service.cate(vo.getCATEGORY_ID()));

        model.addAttribute("pageInfo", vo);
        model.addAttribute("qcri", qcri);

        // 게시판 첨부파일 목록
        List<FilesVO> fileList = service.fileList(QUE_ID);
        model.addAttribute("fileList", fileList);

        // 댓글조회
        List<QsAnswerVO> anlist = anservice.readAnswer(QUE_ID);
        model.addAttribute("anlist", anlist);
    }

    // 질문게시판 수정페이지 진입
    @GetMapping("/qsupdate")
    public void qsUpdateGET(String QUE_ID, QsVO vo, Model model, QsCriteria qcri) {

        vo = service.getpage(QUE_ID);
        vo.setCategory(service.cate(vo.getCATEGORY_ID()));

        model.addAttribute("pageInfo", vo);
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
    public String qsDeletePOST(String QUE_ID, RedirectAttributes rttr) {

        service.delete(QUE_ID);
        rttr.addFlashAttribute("result", "qsdelete success");

        return "redirect:/question/qslist";
    }

    // 질문게시판 첨부파일 다운로드
    @RequestMapping(value = "/fileDown")
    public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = service.filedown(map);
        String fileName = (String) resultMap.get("NAME");

        // 파일을 저장했던 위치에서 첨부파일을 읽어 byte[]형식으로 변환한다.
        byte[] fileBytes = org.apache.commons.io.FileUtils.readFileToByteArray(new File("C:\\save\\file\\" + fileName));

        // 파일명 인코딩
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        // 파일 타입 지정
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 파일 다운로드 설정
        response.setContentType(contentType);
        response.setContentLength(fileBytes.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

        // 파일 전송
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(fileBytes);
        outputStream.flush();
        outputStream.close();
    }

    // 답글 작성
    @PostMapping("/answerWrite")
    public String answerWritePOST(String QUE_ID, QsAnswerVO anvo, QsCriteria qcri, RedirectAttributes rttr) throws IOException {
        log.info("댓글작성 진입");

        if (anservice.writeAnswer(anvo) == 1) {

            notifyAnswer(QUE_ID);

        }

        rttr.addAttribute("que_id", anvo.getQUE_ID());
        rttr.addAttribute("pageNum", qcri.getPageNum());
        rttr.addAttribute("amount", qcri.getAmount());
        rttr.addAttribute("keyword", qcri.getKeyword());
        rttr.addAttribute("type", qcri.getType());

        return "redirect:/question/qsget?QUE_ID=" + QUE_ID;
    }

    private void notifyAnswer(String QUE_ID) throws IOException {
        MemberVO vo = service.selectMember(QUE_ID);
        QsVO qsVo = service.selectQue(QUE_ID);

        firebaseCloudMessageService = new FirebaseCloudMessageService();

        TypeVO type = andService.type("14");
        RequestDTO dto = new RequestDTO();
        dto.setCATEGORY_ID("14");
        dto.setMember_id(vo.getMEMBER_ID());
        andService.insertAlarm(dto);
        if(vo.getALARM().equals("y")){
            firebaseCloudMessageService.sendMessageTo(
                    vo.getTOKEN(),
                    type.getTITLE(),
                    qsVo.getTITLE()+"의"+type.getCONTENT()
            );
        }
    }

}
