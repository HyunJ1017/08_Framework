package edu.kh.daemoim.popup.service;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.common.util.FileUtil;
import edu.kh.daemoim.popup.dto.Popup;
import edu.kh.daemoim.popup.mapper.PopupMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class PopupServiceImpl implements PopupService {
	
	private final PopupMapper mapper;
	
	@Value("${daemoim.mainPopup.web-path}")
	private String mainPopupWebPath;
	
	@Value("${daemoim.mainPopup.folder-path}")
	private String mainPopupFolderPath;
	
	// 팝업 불러오기
	@Override
	public Popup getPopup() {
		List<Popup> popupList = mapper.getPopup();
		
		// bookList가 비어있지 않은 경우 랜덤으로 하나의 책 선택
        if (popupList != null && !popupList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(popupList.size());
            return popupList.get(randomIndex);  // 랜덤으로 선택된 책을 반환
        }

        // bookList가 비어있으면 null 반환 (필요시 예외처리도 가능)
        return null;
	}

	// 팝업 저장하기
	@Override
	public int insertPopup(Popup popup, MultipartFile popupImage) {
		
		String groupMainRename = null;
		
		if(popupImage.isEmpty()) {
			return 0;
		}
		// 파일 리네임
		groupMainRename = FileUtil.rename( popupImage.getOriginalFilename() );
		popup.setPopupRename(groupMainRename);
		popup.setPopupName(popupImage.getOriginalFilename() );
		// 파일 요청경로 저장
		popup.setPopupLocation(mainPopupWebPath);

		// mapper에 모임 등록하고 결과 반환받기
		int result = mapper.insertPopup(popup);
		
		if(result == 0) return 0;
		
		File folder = new File(mainPopupFolderPath);
		if(folder.exists() == false) folder.mkdirs();
		
		try {
			popupImage.transferTo( new File( mainPopupFolderPath + groupMainRename ) );
		} catch (Exception e) {
			result = 0;
			throw new RuntimeException();
		}
		
		return result;
	}
	
	// 리스트 불러오기
	@Override
	public List<Popup> getPopupList() {
		return mapper.getPopup();
	}
	
	// 팝업 불러오기
	@Override
	public Popup selectPopup(int popupNo) {
		List<Popup> popupList = mapper.getPopup();
		Popup popup = null;
		for(Popup p : popupList) {
			if(p.getPopupNo() == popupNo) {
				popup = p;
				break;
			}
		}
		return popup;
	}
	
	// 팝업 삭제하기
	@Override
	public List<Popup> deletePopup(int popupNo) {
		
		int result = mapper.deletePopup(popupNo);
		
		return mapper.getPopup();
	}

}
