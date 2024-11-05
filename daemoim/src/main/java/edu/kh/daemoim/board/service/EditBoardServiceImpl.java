package edu.kh.daemoim.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.dto.BoardImg;
import edu.kh.daemoim.board.mapper.EditBoardMapper;
import edu.kh.daemoim.common.exception.FileUploadFailException;
import edu.kh.daemoim.common.util.FileUtil;
import edu.kh.daemoim.myPage.dto.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:/config.properties")
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EditBoardServiceImpl implements EditBoardService{

	private final EditBoardMapper mapper;
	
	@Value("${daemoim.board.web-path}")
	private String webPath;
	
	@Value("${daemoim.board.folder-path}")
	private String folderPath;
	
	
	// 게시글 작성
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) {
		
		// 게시글 부분 DB에 추가
		int result = mapper.boardInsert(inputBoard);
		System.out.println(inputBoard.getBoardNo());
		
		// 삽입 실패 시
		if(result == 0) return 0;
		
		/* 삽입된 게시글 번호 */
		int boardNo = inputBoard.getBoardNo();
		
		// 실제 업로드한 파일 정보만 모아두는 List
		List<BoardImg> uploadList = new ArrayList<>();
		
		// images 리스트에서 실제 업로드된 파일만 골라내기
		for(int i=0 ; i < images.size(); i++) {
			
			// 제출된 파일이 없을 경우
			if(images.get(i).isEmpty()) continue;
			
			// 있을 경우!
			
			// 파일 원본명
			String boardImgOriginalName = images.get(i).getOriginalFilename();
			
			// 변경된 파일명
			String boardImgRename = FileUtil.rename(boardImgOriginalName);
			
			// DB INSERT를 위한 BoardImg 객체 생성
			BoardImg img 
				= BoardImg.builder()
					.boardImgOriginalName(boardImgOriginalName) 	// 원본명
					.boardImgRename(boardImgRename) 											// 변경명
					.boardImgPath(webPath)  											// 웹 접근 경로
					.boardNo(boardNo)  														// 이미지가 삽입된 게시글 번호
					.boardImgOrder(i)															// 최대 20개의 이미지 칸 중에서 제출된 칸의 순서
					.uploadFile(images.get(i)) 										// 실제 업로드된 이미지 데이터
					.build();
			
			
			// uploadList에 추가
			uploadList.add(img);
			
		} // for end
		
		// 제출된 이미지가 없다면
		if(uploadList.isEmpty()) return boardNo;
		
		
		// 여러 행 한 번에 삽입 후 삽입된 행의 개수 반환
		int insertRows = mapper.insertUploadList(uploadList);
		
		
		// INSERT된 행의 개수와 uploadList의 개수가 같지 않은 경우
		if(insertRows != uploadList.size()) {
			throw new RuntimeException("이미지 INSERT 실패");
			
		} // 앞서 삽입한 게시글 부분도 ROLLBACK 되도록 예외 강제 발생
		try {
			File folder = new File(folderPath);
			if(folder.exists() == false) { // 폴더가 없을 경우
				folder.mkdirs(); // 폴더 생성
			}
			
			// 모두 삽입 성공 시
			// 임시 저장된 파일을 서버에 지정된 폴더 + 변경명으로 저장
			for(BoardImg img : uploadList) {
				img.getUploadFile()
					.transferTo(new File(folderPath + img.getBoardImgRename()));
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new FileUploadFailException(); // 사용자 정의 예외 던짐
		}
		return boardNo;
	}
	
	// 게시글 삭제
	@Override
	public int boardDelete(int boardNo, MyPage loginMember) {
		
		int memberNo = loginMember.getMemberNo();
		
		return mapper.boardDelete(boardNo, memberNo);
	}
	
	// 게시글 수정
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList) {
	// 1. 게시글 부분(제목/내용) 수정
			int result = mapper.boardUpdate(inputBoard);
			if(result == 0) return 0; // 수정 실패 시
			
			
			// 2. 기존에 존재했던 이미지 중
			//    deleteOrderList에 존재하는 순서의 이미지 DELETE
			
			// deleteOrderList에 작성된 값이 있다면
			if(deleteOrderList != null && deleteOrderList.equals("") == false) {
				
				result = mapper.deleteImage(deleteOrderList, inputBoard.getBoardNo());
				
				// 삭제된 행이 없을 경우 -> SQL 실패
				// -> 예외를 발생시켜 전체 rollback
				if(result == 0) {
					throw new RuntimeException("이미지 삭제 실패");
					// 사용자 정의 예외로 바꾸면 더 좋다!!
				}
			}
			
			List<BoardImg> imgList = mapper.selectImageList(inputBoard.getBoardNo());
			List<Integer> oderList = new ArrayList<>();
			for(BoardImg img : imgList) {
				oderList.add( img.getBoardImgOrder() );
			}
			// 3. 업로드된 이미지가 있을 경우
			//    UPDATE 또는 INSERT +  transferTo()
			
			// 실제 업로드된 이미지만 모아두는 리스트 생성
			List<BoardImg> uploadList = new ArrayList<>();
			
			for(int i = 0; i < images.size(); i++) {
				
				// i번째 요소에 업로드된 파일이 없으면 다음으로
				if(images.get(i).isEmpty()) continue;
				
				
				// 업로드된 파일이 있으면
				String originalName = images.get(i).getOriginalFilename();
				String rename = FileUtil.rename(originalName);
				
				// 필요한 모든 값을 저장한 DTO 생성
				BoardImg img = BoardImg.builder()
						.boardImgOriginalName(originalName)
						.boardImgRename(rename)
						.boardImgPath(webPath)
						.boardNo(inputBoard.getBoardNo())
						.boardImgOrder(i)
						.uploadFile(images.get(i))
						.build();
				
				
				// 1행씩 update 수행
				if(oderList.contains(i)) result = mapper.updateImage(img);
				else result = mapper.insertImage(img);
				
				
				// 수정, 삭제가 모두 실패한 경우 --> 말도 안되는 상황
				if(result == 0) {
					throw new RuntimeException("이미지 DB 추가 실패");
				}
				
				uploadList.add(img); // 업로드된 파일 리스트에 img 추가
			}// for end
			
			// 새로운 이미지가 없는 경우
			if(uploadList.isEmpty()) return result;
			
			// 임시 저장된 이미지 파일을 지정된 경로로 이동(transferTo())
			try {
				for(BoardImg img : uploadList) {
					img.getUploadFile().transferTo(new File(folderPath + img.getBoardImgRename()));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new FileUploadFailException();
			}
			
			return result;
	}

	@Override
	public int boardInsert1(Board inputBoard) {
		return mapper.boardInsert(inputBoard);
	}
	
	
}
