package com.shreyam.upload.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.shreyam.upload.model.Employee;
import com.shreyam.upload.service.EmployeeService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeController {
	
	@Autowired
	private EmployeeService service;

	@GetMapping("/home")
	private String home(Model model ) {
		
		List<Employee> list= service.getAllEmployee();
		model.addAttribute("list",list);
		return "index" ;
		
	}
	

	@PostMapping("/upload")
	public String fileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {

	   //if no file is selected  
	    if (file.isEmpty()) {
	        model.addAttribute("error", "Please select a file to upload");
	        System.out.println("no file selected");
	      
	        return "index";
	    }

	    //to check the file size 
	    long fileSize = file.getSize();
	    if (fileSize < 20 * 1024 || fileSize > 20 * 1024 * 1024) {
	        model.addAttribute("error", "File size should be between 20KB and 20MB");
	        System.out.println("not supported");
	        
	        return "index";
	    }

	    //to check the file format allowed or not 
	    String contentType = file.getContentType();
	    if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("application/pdf")) {
	        model.addAttribute("error", "Unsupported file format. Supported formats are JPEG, PNG, and PDF.");
	        System.out.println("not supported");
	        return "index";
	    }

	    Employee employee = new Employee();
	    String fileName = file.getOriginalFilename();
	    employee.setProfilePicture(fileName);
	    employee.setContent(file.getBytes());
	    employee.setSize(fileSize);

	    service.createEmployee(employee);
	    model.addAttribute("success", "File uploaded successfully");

	    // Redirect to the Index page
	    return "index";
	}

	@GetMapping("/downloadfile")
	public void downloadFile(@Param("id") Long id, Model model, HttpServletResponse response) throws IOException {
		
		Optional<Employee> temp=service.findEmployeeById(id);
		
		if(temp!=null) {
			Employee employee=temp.get();
			response.setContentType("application/octet-stream");
			String headerkey="Content-Disposition";
			String headerValue="attachment;filename " + employee.getProfilePicture();
			response.setHeader(headerkey, headerValue);
			
			ServletOutputStream outputStream=response.getOutputStream();
			outputStream.write(employee.getContent());
			outputStream.close();
								
		}
			
}
	
	@GetMapping("/image")
	public void showImage(@Param ("id") Long id,HttpServletResponse response, Optional<Employee> employee) throws IOException {
		employee=service.findEmployeeById(id);
		response.setContentType("image/jpeg, image/jpg, image/png,image/gif, image/pdf");
		response.getOutputStream().write(employee.get().getContent());
		response.getOutputStream().close();		
		
	   }
	@GetMapping("/monitor")
	public String monitor() {
		try {
		    // Define a flag to control the while loop
		   final boolean condition = true;

		    // Create a new thread
		    Thread thread = new Thread(() -> {
		        // Run a while loop until the condition is true
		        while (condition) {
		            try {
		                // Sleep for 5 seconds
		                Thread.sleep(5000);
		            } catch (InterruptedException e) {
		                e.printStackTrace(); // Handle interruption if needed
		            }
		        }
		    });

		    // Start the thread
		    thread.start();

		    // Sleep for 5 seconds in the main thread
		    Thread.sleep(5000);

		    // Set the condition to false to stop the while loop
		    
		} catch (InterruptedException e) {
		    e.printStackTrace(); // Handle interruption if needed
		}

		return "hello world";

	}
		
	
	


}
