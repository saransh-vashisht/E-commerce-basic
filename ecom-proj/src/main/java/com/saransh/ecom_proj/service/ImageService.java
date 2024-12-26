package com.saransh.ecom_proj.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saransh.ecom_proj.model.Product;

@Service
public class ImageService {
	
	private static final String IMAGE_STORAGE_PATH="C://temp";

	//To save the image 
	public String saveImagetoStorage(MultipartFile imageFile) throws IOException{
		//Create upload path if it does not exist
		Path uploadPath=Path.of(IMAGE_STORAGE_PATH);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		//Extracting the file name
		String imgName=imageFile.getOriginalFilename();
		String fileExtension=getFileExtension(imgName);
		String firstName=getFirstName(imgName); 
		String currentTime=getCurrentTime().replace(":", "-");
		
		//New Unique file Name
		String updatedName=firstName+currentTime+fileExtension;
		
		//	Path filePath=uploadPath.resolve(imageFile.getOriginalFilename());
		//Full File Path using Path.resolve()
		Path filePath=uploadPath.resolve(updatedName);

		Files.write(filePath, imageFile.getBytes());
			//Files.copy(imageFile.getInputStream(), filePath,StandardCopyOption.REPLACE_EXISTING);
			return updatedName;
	}
	
	
	//To view an image
	public byte[] getImage(Product product) throws IOException {
		String imageName=product.getUpdatedImageName();
	
		Path imagePath=Path.of(IMAGE_STORAGE_PATH, imageName);
		if (Files.exists(imagePath)) {
			byte[] imageBytes=Files.readAllBytes(imagePath);
			return imageBytes;
		} else {
			return null; //Handles missing images
		}	
	}
	
	//To update an image
		public String updateImageInStorage(String fileName,MultipartFile imageFile) throws IOException {
			if(imageFile == null || imageFile.isEmpty()) {
				return null;
			}
			
			//Create upload path if it does not exist
			Path uploadPath=Path.of(IMAGE_STORAGE_PATH);
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			//Deleting the old file
			deleteImage(fileName);
//			Path oldImagePath=uploadPath.resolve(fileName);
//			if(Files.exists(oldImagePath)) {
//				Files.delete(oldImagePath);
//			}
			
			//Updating the new File 
			//Extracting the file name
			String imgName=imageFile.getOriginalFilename();
			String fileExtension=getFileExtension(imgName);
			String firstName=getFirstName(imgName); 
			String currentTime=getCurrentTime().replace(":", "-");
			
			//New Unique file Name
			String updatedName=firstName+currentTime+fileExtension;
			
			Path filePath=uploadPath.resolve(updatedName);
			Files.write(filePath, imageFile.getBytes());
			
			return updatedName;
		}
		

		public void deleteImage(String fileName) throws IOException {
			//Create upload path if it does not exist
			Path uploadPath=Path.of(IMAGE_STORAGE_PATH);
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			Path ImagePath=uploadPath.resolve(fileName);
			if(Files.exists(ImagePath)) {
				Files.delete(ImagePath);
			}
			
			
		}
		
		
		
		//Utility method to get the file extenstion from the file name
		private String getFileExtension(String filename) {
			int lastIndexOfDot=filename.lastIndexOf(".");
			if (lastIndexOfDot==-1) {
				return ""; //No extension found
			}
			return filename.substring(lastIndexOfDot);
		}
		
		private String getFirstName(String filename) {
			if (filename==null || filename.isEmpty()) {
				throw new IllegalArgumentException("Filename cannot be empty");
				
			}
			int dotIndex=filename.lastIndexOf('.');
			if (dotIndex==-1) {
				return filename;
			}
			
			return filename.substring(0, dotIndex);
		}
		
		private String getCurrentTime() {
			//Getting the currrent Time 
			LocalDateTime currentTime=LocalDateTime.now();
			DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedTime=currentTime.format(formatter);
			return formattedTime;
		}


		
}
