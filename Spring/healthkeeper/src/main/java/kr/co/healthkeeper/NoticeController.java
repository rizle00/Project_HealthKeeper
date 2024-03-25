package kr.co.healthkeeper;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.mapper.NoticeMapper;
import kr.co.service.NoticeService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/notice/*")
public class NoticeController {
	
	@Autowired
	private NoticeService service;

    @Autowired
    private AndMemberService andService;

    private FirebaseCloudMessageService firebaseCloudMessageService;
	
	// log 메서드 사용
	private static final Logger log = LoggerFactory.getLogger(NoticeController.class);
	
	// 공지사항 목록페이지(페이징 적용)
	@GetMapping("/notlist")
    public void noticeListGET(Model model, NotCriteria ncri) {
		
        model.addAttribute("notlist", service.getListPaging(ncri));
        
        int total = service.getTotal(ncri);
        NotPageMakeDTO npageMake = new NotPageMakeDTO(ncri, total);
        model.addAttribute("npageMake",npageMake);
    }
    
	// 공지사항 등록페이지 
    @GetMapping("/notregistr")
    public void noticeRegistrGET() {
        log.info("공지사항 등록 페이지 진입");
    }
	
    // 공지사항 등록
    @PostMapping("/notregistr")
    public String noticeRegistrPOST(NoticeVO notice, RedirectAttributes rttr,
    	MultipartHttpServletRequest notRequest,Model model)throws Exception {
    	
    	if(service.notregistr(notice,notRequest) != 0){
            createAlarm();

        }
    	
    	// 공지사항을 등록하고 , 멤버의 이름이 함께 저장되도록 함
    	service.notregistr(notice,notRequest);
    	rttr.addFlashAttribute("result", "registr success");
    	return "redirect:/notice/notlist";
    }

    private void createAlarm() throws IOException {

        firebaseCloudMessageService = new FirebaseCloudMessageService();
        List<MemberVO> list = andService.memberList();
        TypeVO type = andService.type("13");
        RequestDTO dto = new RequestDTO();
        for(int i = 0; i<list.size(); i++){
           if(!list.get(i).getROLE().equals("admin")){
               dto.setCATEGORY_ID("13");
               dto.setMember_id(list.get(i).getMEMBER_ID());
               andService.insertAlarm(dto);
               if(list.get(i).getALARM().equals("y")){
                   firebaseCloudMessageService.sendMessageTo(
                           list.get(i).getTOKEN(),
                           type.getTITLE(),
                           type.getCONTENT()

                   );
               }
           }

        }
    }


    // 공지사항 조회
    @GetMapping("/notget")
    public void noticeGetPageGET(String NOTICE_ID, Model model, NotCriteria ncri)throws Exception {
    public void noticeGetPageGET(int NOTICE_ID, NoticeVO vo,Model model, NotCriteria ncri)throws Exception {

    	// 공지사항 조회수
    	service.noticeViews(NOTICE_ID);
    	
    	vo = service.getPage(NOTICE_ID);
    	vo.setMember(service.member(vo.getMEMBER_ID()));
    	
        model.addAttribute("pageInfo", vo);
        model.addAttribute("ncri", ncri);
        
        // 공지사항 첨부파일 목록
    	List<FilesVO> fileList = service.selectFileList(NOTICE_ID);
    	model.addAttribute("fileList", fileList);
    
    }
    
    // 공지사항 수정페이지 이동
    @GetMapping("/notmodify")
    public void noticeModifyGET(String NOTICE_ID, Model model, NotCriteria ncri) {
        
        model.addAttribute("pageInfo", service.getPage(NOTICE_ID));
        model.addAttribute("ncri", ncri);
        
        // 공지사항 첨부파일 목록
    	List<FilesVO> fileList = service.selectFileList(NOTICE_ID);
    	model.addAttribute("fileList", fileList);
    	
    	// 공지사항 첨부파일 수정업로드
//    	List<FilesVO> updatefile = service.updateFileList(NOTICE_ID);
//    	model.addAttribute("updatefile",updatefile);
    	
    }
    
    // 공지사항 수정
    @PostMapping("/notmodify")
    public String noticeModifyPOST(NoticeVO notice, RedirectAttributes rttr
                                   ) throws Exception {

        // 파일 삭제 파라미터 추출
        //String[] files = request.getParameterValues("fileIdDel[]");
        //String[] fileNames = request.getParameterValues("fileNameDel[]");

        // 첨부파일 삭제
        //int NOTICE_ID = notice.getNOTICE_ID();
        //service.deleteFile(NOTICE_ID);


        // 공지사항 수정
        service.notmodify(notice);

        // 공지사항 수정에 성공한 경우에만 삭제된 파일 ID 목록을 모델에 추가
        //rttr.addFlashAttribute("result", "notmodify success");
        return "redirect:/notice/notlist";
    }

    
    // 공지사항 삭제
    @PostMapping("/notdelete")
    public String notDeletePOST(String NOTICE_ID, RedirectAttributes rttr) {
        
        service.notdelete(NOTICE_ID);
        rttr.addFlashAttribute("result", "notdelete success");
        return "redirect:/notice/notlist";
    }
    
    // 공지사항 파일 다운로드
    @RequestMapping(value="/fileDown")
    public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = service.selectFileInfo(map);
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
    
    // 첨부파일 삭제
    @PostMapping("/deleteFile")
    public String deleteFile(String NOTICE_ID) throws Exception {
        // 파일 삭제 작업 수행
       service.deleteFile(NOTICE_ID);
       return "redirect:/notice/notget?NOTICE_ID="+ NOTICE_ID; // 파일 삭제 후 다시 조회 페이지로 리다이렉트
    }

}
