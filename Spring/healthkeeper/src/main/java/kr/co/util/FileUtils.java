package kr.co.util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.model.NoticeVO;

@Component("fileUtils")
public class FileUtils {
	private static final String FILE_PATH = "C:\\save\\file"; // 파일이 저장될 위치
	
	
	// 첨부파일 추가 관련 로직
	public List<Map<String, Object>> parseInsertFileInfo(NoticeVO notice, 
            MultipartHttpServletRequest notRequest) throws IOException {
        Iterator<String> iterator = notRequest.getFileNames();

        List<Map<String, Object>> list = new ArrayList<>();
        while (iterator.hasNext()) {
            MultipartFile multipartFile = notRequest.getFile(iterator.next());
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String FILE_NAME = multipartFile.getOriginalFilename(); // 파일명 가져오기
                String savedFileName = getRandomString() + "_" + FILE_NAME; // 저장될 파일명 생성
                String filePath = FILE_PATH + File.separator + savedFileName; // 파일 경로 설정

                File file = new File(FILE_PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }

                // 파일 저장
                try {
                    multipartFile.transferTo(new File(filePath));
                } catch (IOException e) {
                    throw new IOException("Failed to save file: " + FILE_NAME);
                }

                // 파일 정보를 Map에 추가
                Map<String, Object> fileMap = new HashMap<>();
                fileMap.put("FILE_NAME", FILE_NAME);
                fileMap.put("FILE_PATH", filePath);
                fileMap.put("FILE_SIZE", multipartFile.getSize());
                fileMap.put("NOTICE_ID", notice.getNOTICE_ID());
                list.add(fileMap);
            }
        }
        return list;
    }

    // 파일 업데이트를 위한 파일 정보 파싱 메서드는 그대로 유지됩니다.

    // getRandomString 메서드도 그대로 유지됩니다.

	
	// 파일 추가 메서드 (수정용)
	 public List<Map<String, Object>> parseUpdateFileInfo(NoticeVO notice, String[] files, String[] fileNames,
	            MultipartHttpServletRequest mpRequest) throws Exception {
	        Iterator<String> iterator = mpRequest.getFileNames();
	        MultipartFile multipartFile = null;
	        String FILE_NAME = null;
	        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	        Map<String, Object> listMap = null;
	        int NOTICE_ID = notice.getNOTICE_ID();

	        while (iterator.hasNext()) {
	            multipartFile = mpRequest.getFile(iterator.next());
	            if (multipartFile != null && !multipartFile.isEmpty()) {
	                FILE_NAME = multipartFile.getOriginalFilename();
	                String savedFileName = getRandomString() + "_" + FILE_NAME; // 저장될 파일명 생성
	                String filePath = FILE_PATH + File.separator + savedFileName; // 파일 경로 설정

	                File file = new File(FILE_PATH);
	                if (!file.exists()) {
	                    file.mkdirs();
	                }

	                // 파일 저장
	                try {
	                    multipartFile.transferTo(new File(filePath));
	                } catch (Exception e) {
	                    throw new Exception("Failed to save file: " + FILE_NAME);
	                }

	                // 파일 정보를 Map에 추가
	                Map<String, Object> fileMap = new HashMap<>();
	                fileMap.put("FILE_NAME", FILE_NAME);
	                fileMap.put("FILE_PATH", filePath);
	                fileMap.put("FILE_SIZE", multipartFile.getSize());
	                fileMap.put("NOTICE_ID", NOTICE_ID);
	                list.add(fileMap);
	            }
	        }

	        // 기존 파일 정보 추가
	        if (files != null && fileNames != null) {
	            for (int i = 0; i < fileNames.length; i++) {
	                listMap = new HashMap<String, Object>();
	                listMap.put("IS_NEW", "N");
	                listMap.put("FILE_NO", files[i]);
	                list.add(listMap);
	            }
	        }

	        return list;
	    }
	
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}