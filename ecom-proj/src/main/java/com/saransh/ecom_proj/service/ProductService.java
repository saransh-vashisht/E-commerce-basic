package com.saransh.ecom_proj.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saransh.ecom_proj.exceptions.ProductNotFoundException;
import com.saransh.ecom_proj.model.Product;
import com.saransh.ecom_proj.repo.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepo repo;
	
	@Autowired
	private ImageService imgservice;

	public List<Product> getAllProducts() {
		return repo.findAll();
	}

	public Product getProductById(int id) {
		return repo.findById(id)
				   .orElseThrow(() -> new ProductNotFoundException("Product with id "+ id+ " not found"));
	}

	public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
		product.setImageName(imageFile.getOriginalFilename());
		product.setImagetype(imageFile.getContentType());
		
		String updatedImageName= imgservice.saveImagetoStorage(imageFile);
		product.setUpdatedImageName(updatedImageName);
		
		
		return repo.save(product);	
	}


	public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
		Product existingProduct=repo.findById(id).orElse(null);
		String fileName=existingProduct.getUpdatedImageName();
		product.setImageName(imageFile.getOriginalFilename());
		product.setImagetype(imageFile.getContentType());
		String updatedFileName=imgservice.updateImageInStorage(fileName,imageFile);
		product.setUpdatedImageName(updatedFileName);
		
		return repo.save(product);
	}

	public void deleteProduct(int id, Product product) throws IOException {
		String fileName=product.getUpdatedImageName();
		imgservice.deleteImage(fileName);
		repo.deleteById(id);
		
	}

	public List<Product> searchProducts(String keyword) {
		
		return repo.searchProducts(keyword);
	}



}
