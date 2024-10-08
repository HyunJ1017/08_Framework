package edu.kh.project.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.BoardImg;
import edu.kh.project.board.mapper.EditBoardMapper;
import edu.kh.project.common.exception.FileUploadFailException;
import edu.kh.project.common.util.FileUtil;
import lombok.RequiredArgsConstructor;

@PropertySource("classpath:/config.properties")
@Service
@Transactional
@RequiredArgsConstructor
public class EditBoardServiceImpl implements EditBoardService {
	
	private final EditBoardMapper mapper;
	
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	

	// 게시글 등록
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) {
		// 1. 게시글 부분(제목, 내용, 작성자, 게시판종류) INSERT
		//    -> 동적 SQL 사용으로 게시글 번호가 inputBoard에 세팅됨
		// 2) 실제로 업로드된 이미지만 모아두기
		// 3. DB에 uploadList에 저장된 값 모두 insert
		// 		+ transferTo() 수행해서 파일 저장
		
		
		// 1. 게시글 부분(제목, 내용, 작성자, 게시판종류) INSERT
		// -> 동적 SQL 사용으로 게시글 번호가 inputBoard에 세팅됨
		int result = mapper.boardInsert(inputBoard);
		
		// 삽입 실패시
		if(result == 0) return 0;
		
		/* 삽입된 게시글 번호 */
		int boardNo = inputBoard.getBoardNo();
		
		//------------------------------------------------------
		
		// 2) 실제로 업로드된 이미지만 모아두기
		
		// 실제 업로들된 파일 정보만 모아두는 List
		List<BoardImg> uploadList = new ArrayList<>();
		
		// 실제 업로드된 파일 골라내기(null 파일 만들지 않으려고)
		// 이름, 파일크기, isempty 등등 다 써도 됨
		for(int i=0; i < images.size(); i++) {
			
			// 제출된 파일이 없을 경우
			if(images.get(i).isEmpty()) continue;
			
			// 있을경우!
			
			// 파일 원본명
			String originalName = images.get(i).getOriginalFilename();
			
			// 변경된 파일명
			String rename = FileUtil.rename(originalName);
			
			// DB INSERT를 위한 BoardImg 객체 생성
			BoardImg img
				= BoardImg.builder()
					.imgOriginalName(originalName)
					.imgRename(rename)
					.imgPath(webPath)
					.boardNo(boardNo)
					.imgOrder(i)
					.uploadFile(images.get(i))
					.build();
			
			// 리스트에 객체 추가
			uploadList.add(img);
		}
		
		// 제출된 이미지가 없어서 리스트에 하나도 추가가 안됬다면
		if(uploadList.isEmpty()) return boardNo;
		
		// ----------------------------------------------------
		
		// 3. DB에 uploadList에 저장된 값 모두 insert
		// 		+ transferTo() 수행해서 파일 저장
		
		/* [List에 저장된 내용 INSERT 하는 방법]
		 * 1. 1행을 삽입하는 MAPPER 메서드를 여러번 호출
		 * 2. 여러행을 삽입하는 SQL 1회 호출
		 * 		(복잡한 SQL + 동적 SQL)(이걸로!!!!!!!!!!)
		 */
		int insertRows = mapper.insertUploadList(uploadList);
		
		// 결과 검사
		// INSERT된 행의 갯수와 uploadList의 개수가 같지 않은경우
		if(insertRows != uploadList.size()) {
			throw new RuntimeException("이미지 INSERT 실패");
			// 앞서 삽입한 게시글 부분도 ROLLBACK 되도록 예외 강제 발생
			// 사용자 정의 예외로 바꿀 예정
		}
		
		// 모두 삽입 성공시
		// 임시 저장된 파일을 서버에 지정된 폴더 + 변경명으로 저장
		try {
			
			// 폴더경로가없다면 생성하기
			File folder = new File(folderPath);
			if(folder.exists() == false) folder.mkdirs();
			
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadFailException(); // 사용자 정의 예외 던짐
		}
		
		return boardNo;
	}
	
	// 게시글 삭제
	@Override
	public int boardDelete(Board board) {
		return mapper.boardDelete(board);
	}
	
	// 게시글 수정
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList) {
		/* 
		 1. 게시글 부분(제목, 내용) 수정
		 2. 기존에 존재했던 이미지 중 deleteOrderList에 존재하는 순서의 이미지 DELETE
		 3. 업로드된 이미지가 있을 경우 UPDATE 또는 INSERT
		    + transferTo()
		 */ 
		
		// 1. 게시글 부분(제목, 내용) 수정
		int result = mapper.boardUpdate(inputBoard);
		
		if(result == 0) return 0; // 수정 실패
		
		// 2. 기존에 존재했던 이미지 중 deleteOrderList에 존재하는 순서의 이미지 DELETE
		// deleteOrderList에 작성된게 있다면
		if(deleteOrderList != null && deleteOrderList.equals("") == false) {
			
			result = mapper.deleteImg(deleteOrderList, inputBoard.getBoardNo());
		}
		
		// 삭제된 행이 없을경우 -> SQL 실패
		// -> 예외를 발생시켜 전체 롤백
		if(result == 0) {
			throw new RuntimeException("이미지 삭제 실패");
			// 사용자 정의 예외로 바꾸면 더 좋음
		}
		
		
		// 3. 업로드된 이미지가 있을 경우 UPDATE 또는 INSERT + transferTo()
		
		// 실제 업로드된 이미지만 모으기
		List<BoardImg> uploadList = new ArrayList<>();
		
		for(int i=0 ; i < images.size(); i++) {
			
			// i번째 요소에 업로드된 파일이 없으면 다음으로
			if(images.get(i).isEmpty()) continue;
			
			String originalName = images.get(i).getOriginalFilename();
			String rename = FileUtil.rename(originalName);
			
			// 데이터 전달용 DTO 생성
			BoardImg img = BoardImg.builder()
					.imgOriginalName(originalName)
					.imgRename(rename)
					.imgPath(webPath)
					.boardNo(inputBoard.getBoardNo())
					.imgOrder(i)
					.uploadFile(images.get(i))
					.build();
			
			// 1행 update
			// oracle은 각각의 인스턴스에 다중 업데이트 안됨 -> 하나씩 진행
			result = mapper.updateImg(img);
			
			// 수정이 실패한 경우 : 기존 이미지가 없음 == 새로운 이미지다
			if(result == 0) {
				result = mapper.insertImg(img);
			}
			
			// 수정, 삭제가 모두 실패한 경우
			if(result == 0) {
				throw new RuntimeException("이미지 업로드 실패");
			}
			
			uploadList.add(img); // 업로드된 파일 리스트에 img 추가
			
		} // for end
		
		
		// 새로운 이미지가 없는 경우
		if(uploadList.isEmpty()) return result;
		
		
		try {
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileUploadFailException();
		}
		
		
		return result;
	}

}
