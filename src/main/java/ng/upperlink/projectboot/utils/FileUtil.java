package ng.upperlink.projectboot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ng.upperlink.projectboot.exceptions.InvalidFileException;


@Component
public class FileUtil {
	@Value("${upload.file.extensions}")
	private String validExtensions;

	@Value("${resume.file.extensions}")
	private String validResumeExtensions;
	
	public String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf(".");
		if (dotIndex < 0) {
			return null;
		}
		return fileName.substring(dotIndex + 1);
	}

	public boolean isValidExtension(String fileName) throws InvalidFileException {
		String fileExtension = getFileExtension(fileName);

		if (fileExtension == null) {
			throw new InvalidFileException("No File Extension");
		}

		fileExtension = fileExtension.toLowerCase();

		for (String validExtension : validExtensions.split(",")) {
			if (fileExtension.equals(validExtension)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValidResumeExtension(String fileName) throws InvalidFileException {
		String fileExtension = getFileExtension(fileName);

		if (fileExtension == null) {
			throw new InvalidFileException("No File Extension");
		}

		fileExtension = fileExtension.toLowerCase();

		for (String validExtension : validResumeExtensions.split(",")) {
			if (fileExtension.equals(validExtension)) {
				return true;
			}
		}
		return false;
	}
}
