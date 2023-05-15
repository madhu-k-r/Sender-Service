package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
//import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Service
@SpringBootApplication
@Component
@EnableDiscoveryClient
public class SenderServiceApplication
{
//	 @Autowired
//     private RestTemplate restTemplate;
//	
//	 @Bean
//	 @LoadBalanced
//	 public RestTemplate restTemplate() 
//	 {
//		 return new RestTemplate();
//	 }
	
	@Autowired
	private static RestTemplate restTemplate;

	//@Autowired
    public SenderServiceApplication(RestTemplate restTemplate) {
        SenderServiceApplication.restTemplate = restTemplate;
    }

	//@Scheduled(fixedRate=2000) // run every 2 seconds
    //@Override
    public static void main(String[] args)
    {
       SenderServiceApplication sp = new SenderServiceApplication(restTemplate);
       SpringApplication.run(SenderServiceApplication.class, args);
       int i=0,j=120;
       //String putVal="1",deleteVal="3";//postVal="2";
       String getByIdVal = "1", putVal="2",deleteVal="1";
       while(i<j)
       {
          sp.getRequest();
          sp.postRequest();
          sp.getByIdRequest(getByIdVal);      //
          sp.updateByIdRequest(putVal);
          sp.deleteByIdRequest(deleteVal);
          i++;
          int newValue1 = Integer.parseInt(putVal) + 1;
          int newValue2 = Integer.parseInt(deleteVal) + 1;
          int newValue3 = Integer.parseInt(getByIdVal) + 1;   //
 
          String updatedValue1 = String.valueOf(newValue1);
          String updatedValue2 = String.valueOf(newValue2);
          String updatedValue3 = String.valueOf(newValue3);   //

          putVal = updatedValue1;
          deleteVal = updatedValue2;
          getByIdVal = updatedValue3;    //
          try
          {
             Thread.sleep(2000); // sleep for 2 seconds
          }
          catch(InterruptedException e)
          {
             System.out.println("Exception: "+e);
          }
       }
    }

    public void getRequest()
    {
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<String> request = new HttpEntity<String>("Getting Data", headers);

       String result = restTemplate.getForObject("http://localhost:8083/getall", String.class, request);

//       //
//       List<String> lastIndex = result.lines().toList();
//       System.out.println("Latest Index: "+lastIndex);
//       //
       
       System.out.println("Getting all Data: "+result);
    }

    public void postRequest()
    {
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       //String requestJson = "{\"name\":\"Anime\", \"role\":\"One Piece\"}";
       String requestJson = "{\"name\":\"Employee\", \"role\":\"Developer\"}";
       HttpEntity<String> request = new HttpEntity<String>(requestJson, headers);
     
       String result = restTemplate.postForObject("http://localhost:8083/add", request, String.class);
       System.out.println("Inserting Data: "+result);
    
       }
    
    public void getByIdRequest(String getByIdVal)
    {
       Map < String, String > params = new HashMap < String, String > ();
       params.put("id", getByIdVal);   //

       String result = restTemplate.getForObject("http://localhost:8083/getone/{id}", String.class, params);

       System.out.println("Getting Data By Id: "+result);

    }

    public void updateByIdRequest(String putVal)
    {
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       //String requestJson = "{\"id\":\"2\", \"name\":\"Joyboy\", \"role\":\"Mystery\"}";
       String requestJson = "{\"name\":\"Employer\", \"role\":\"CEO\"}";
       HttpEntity<String> request = new HttpEntity<String>(requestJson, headers);

       Map < String, String > params = new HashMap < String, String > ();
       params.put("id", putVal);

       restTemplate.put("http://localhost:8083/update/{id}", request, params);
    }

    public void deleteByIdRequest(String deleteVal)
    {
       Map < String, String > params = new HashMap < String, String > ();
       params.put("id", deleteVal);

       restTemplate.delete("http://localhost:8083/delete/{id}", params);

    }

 }

