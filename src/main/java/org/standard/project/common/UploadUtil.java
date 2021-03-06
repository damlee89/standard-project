package org.standard.project.common;

import java.util.UUID;

import org.springframework.util.FileCopyUtils;
import net.coobird.thumbnailator.Thumbnails;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

public class UploadUtil {
	static final int THUMB_WIDTH = 300;
	static final int THUMB_HEIGHT = 300;
	//파일 업로드 메소드
	public static String fileUpload(String uploadPath, String fileName, byte[] fileData, String ymdPath)
			throws Exception {
		//무작위 uuid생성
		UUID uid = UUID.randomUUID();
		//업로드되는 파일 이름을 무작위uid+파일이름으로 겹치기 힘들도록 생성
		String newFileName = uid + "_" + fileName;
		//이미지 패스 설정
		String imgPath = uploadPath + ymdPath;
		
		File target = new File(imgPath, newFileName);
		FileCopyUtils.copy(fileData, target);
		//썸네일 생성부분, 썸네일 이름은 s_(uid)_(업로드파일이름)
		String thumbFileName = "s_" + newFileName;
		//해당 루트에 이미지 저장.
		File image = new File(imgPath + File.separator + newFileName);
		//해당 루트에 썸네일 저장
		File thumbnail = new File(imgPath + File.separator + "s" + File.separator + thumbFileName);
		//이미지가 저장되면 썸네일 생성.
		if (image.exists()) {
			thumbnail.getParentFile().mkdirs();
			Thumbnails.of(image).size(THUMB_WIDTH, THUMB_HEIGHT).toFile(thumbnail);
		}
		return newFileName;
	}
	//현재 날짜 기준으로 폴더를 만들어주는 메소드.
	public static String calcPath(String uploadPath) {
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		
		makeDir(uploadPath, yearPath, monthPath, datePath);
		makeDir(uploadPath, yearPath, monthPath, datePath + "\\s");

		return datePath;
	}
	//폴더 생성
	private static void makeDir(String uploadPath, String... paths) {

		if (new File(paths[paths.length - 1]).exists()) {
			return;
		}

		for (String path : paths) {
			File dirPath = new File(uploadPath + path);

			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}
}
