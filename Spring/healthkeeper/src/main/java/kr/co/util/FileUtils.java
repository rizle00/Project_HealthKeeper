package kr.co.util;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.model.NoticeVO;
import kr.co.model.QsVO;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component("fileUtils")
public class FileUtils {
	private static final String PATH = "C:\\save\\file"; // 파일이 저장될 위치
	
	
	// 공지사항 게시판 첨부파일 추가
	public List<Map<String, Object>> parseInsertFileInfo(NoticeVO notice, 
	        MultipartHttpServletRequest notRequest) throws Exception {
	    Iterator<String> iterator = notRequest.getFileNames();
	    
	    MultipartFile multipartFile = null;
	    String NAME = null;
	    
	    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	    Map<String, Object> listMap = null;
	    
	    // NoticeVO에서 NOTICE_BNO 가져오기
	    int NOTICE_ID = notice.getNOTICE_ID();
	    
	    File file = new File(PATH);
	    if(!file.exists()) {
	        file.mkdirs();
	    }
	    
	    while(iterator.hasNext()) {
	        multipartFile = notRequest.getFile(iterator.next());
	        if(!multipartFile.isEmpty()) {
	            NAME = multipartFile.getOriginalFilename(); // 파일명 가져오기
	            
	            String savedFileName = getRandomString() + "_" + NAME; // 저장될 파일명 생성
	            
	            File saveFile = new File(PATH, savedFileName); // 저장될 파일 객체 생성
	            multipartFile.transferTo(saveFile); // 파일 저장
	            
	            listMap = new HashMap<String, Object>();
	            listMap.put("NAME", NAME);
	            listMap.put("PATH", PATH);
	            listMap.put("NOTICE_ID", NOTICE_ID); // NOTICE_BNO 추가
	            list.add(listMap);
	        }
	    }
	    return list;
	}

	
	// 질문게시판 첨부파일 추가
	public List<Map<String, Object>> parseInsertFileInfo(QsVO qs, 
	        MultipartHttpServletRequest qsRequest) throws Exception {
	    Iterator<String> iterator = qsRequest.getFileNames();
	    
	    MultipartFile multipartFile = null;
	    String NAME = null;
	    
	    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	    Map<String, Object> listMap = null;
	    
	    // QsVO에서 QUE_ID 가져오기
	    int QUE_ID = qs.getQUE_ID();
	    
	    File file = new File(PATH);
	    if(!file.exists()) {
	        file.mkdirs();
	    }
	    
	    while(iterator.hasNext()) {
	        multipartFile = qsRequest.getFile(iterator.next());
	        if(!multipartFile.isEmpty()) {
	            NAME = multipartFile.getOriginalFilename(); // 파일명 가져오기
	            
	            String savedFileName = getRandomString() + "_" + NAME; // 저장될 파일명 생성
	            
	            File saveFile = new File(PATH, savedFileName); // 저장될 파일 객체 생성
	            multipartFile.transferTo(saveFile); // 파일 저장
	            
	            listMap = new HashMap<String, Object>();
	            listMap.put("NAME", NAME);
	            listMap.put("PATH", PATH);
	            listMap.put("QUE_ID", QUE_ID); // QUE_ID 추가
	            list.add(listMap);
	        }
	    }
	    return list;
	}
	
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}