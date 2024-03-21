package kr.co.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.mapper.QsMapper;
import kr.co.util.FileUtils;

@Service
public class QsServiceImpl implements QsService{
	
	@Autowired
	private QsMapper mapper;
	
	@Resource
	private FileUtils fileUtils;

	// 게시판 등록
	@Override
	public void qsregistr(QsVO qs, MultipartHttpServletRequest qsRequest) throws Exception{
		mapper.qsregistr(qs);
		
		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(qs, qsRequest);
		int size = list.size();
		for(int i=0; i<size; i++) {
			mapper.insertfile(list.get(i));
		}
	}

	// 게시판 목록
	@Override
	public List<QsVO> getlist() {
		return mapper.getlist();
	}
	
	// 게시판 목록(페이징 처리)
	@Override
	public List<QsVO> getlistPaging(QsCriteria qcri) {
		return mapper.getlistPaging(qcri);
	}
	
	// 게시판 글 총 갯수
	@Override
	public int getTotal(QsCriteria qcri) {
		return mapper.getTotal(qcri);
	}

	// 게시판 조회
	@Override
	public QsVO getpage(String QUE_ID) {
		return mapper.getpage(QUE_ID);
	}

	// 게시판 수정
	@Override
	public int update(QsVO qs) {
		return mapper.update(qs);
	}

	// 게시판 삭제
	@Override
	public int delete(String QUE_ID) {
		return mapper.delete(QUE_ID);
	}

	// 게시판 조회수
	@Override
	public int qsViews(String QUE_ID) {
		return mapper.qsViews(QUE_ID);
	}

	// 카테고리 리스트
	@Override
	public List<CateGoryVO> catelist() throws Exception{
		return mapper.catelist();
	}

	@Override
	public CateGoryVO cate(String CATEGORY_ID) {
		return mapper.cate(CATEGORY_ID);
	}

	// 게시판 첨부파일 조회
	@Override
	public List<FilesVO> fileList(String QUE_ID) {
		return mapper.fileList(QUE_ID);
	}

	// 게시판 첨부파일 다운로드
	@Override
	public Map<String, Object> filedown(Map<String, Object> map) throws Exception{
		return mapper.filedown(map);
	}

	@Override
	public MemberVO selectMember(String QUE_ID) {
		return mapper.selectMember(QUE_ID);
	}

	@Override
	public QsVO selectQue(String QUE_ID) {
		return mapper.selectQue(QUE_ID);
	}

}
