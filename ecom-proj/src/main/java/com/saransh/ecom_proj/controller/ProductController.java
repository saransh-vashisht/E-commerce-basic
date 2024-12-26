package com.saransh.ecom_proj.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saransh.ecom_proj.model.Product;
import com.saransh.ecom_proj.service.ImageService;
import com.saransh.ecom_proj.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
	
	private final ProductService service;
	private final ImageService imgservice;
	 
	public ProductController(ProductService service, ImageService imgservice) {
		this.service=service;
		this.imgservice=imgservice;
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts(){
		return new ResponseEntity<>(service.getAllProducts(),HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id) {
		Product product= service.getProductById(id);
		if (product != null) {
				return new ResponseEntity<>(product,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	//we can use the requestbody if it is only a product
	//so we need to use the image as well of different format
	//to process this we will use the requestpart
	//it accepts in multiple parts
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product,
			@RequestPart MultipartFile imageFile ){

		try {
			Product product1 =service.addProduct(product,imageFile);
			return new ResponseEntity<>(product1,HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	@GetMapping("/product/{id}/image")
	public ResponseEntity<byte[]> getImageByProductId(@PathVariable int id){
		Product product= service.getProductById(id);
		byte[] imageFile;
		try {
			imageFile = imgservice.getImage(product);
			return ResponseEntity.ok()
					.contentType(MediaType.valueOf(product.getImagetype()))
					.body(imageFile);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);								
		}
		
		
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product, @RequestPart MultipartFile imageFile){
	Product product1;
	try {
		product1 = service.updateProduct(id,product,imageFile);
	} catch (IOException e) {
		return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
	}
	if(product1 !=null) {
		return new ResponseEntity<>("Updated",HttpStatus.OK);
	}else {
		return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
	}
		
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id){
		Product product=service.getProductById(id);
		if (product!=null) {
			try {
				service.deleteProduct(id,product);
				return new ResponseEntity<>("Deleted",HttpStatus.OK);
			
			} catch (IOException e) {
				return new ResponseEntity<>("Directory not Created",HttpStatus.NOT_FOUND);
			}
			
		}else {
			return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(String keyword){
		System.out.println("Searching with "+ keyword);
		List<Product> products=service.searchProducts(keyword);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
}
