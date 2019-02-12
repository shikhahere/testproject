package springapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import valueobjects.VersionInfo;

@RestController
public class VersionChecker {
	
	private static String PWS_URL = "https://api.run.pivotal.io/v2/info";
	private static String BLUEMIX_URL = "https://api.ng.bluemix.net/v2/info";
	private static String PWS_EXPECTEDVERSION = "2.63.0";
	private static String BLUEMIX_EXPECTEDVERSION = "2.54.0";

	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/pws/validVersion")
	//@ExceptionHandler
    public ResponseEntity<Boolean> isValidPwsVersion() {
		ResponseEntity<VersionInfo> versionInfo = restTemplate.getForEntity(PWS_URL, VersionInfo.class);
		if (HttpStatus.OK.equals(versionInfo.getStatusCode())) {
	       	return ResponseEntity.status(HttpStatus.OK).body(versionInfo.getBody() != null ?
	       			PWS_EXPECTEDVERSION.equalsIgnoreCase(versionInfo.getBody().getApiVersion()) : null);
		} 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
	
	@RequestMapping("/bluemix/validVersion")
    public ResponseEntity<Boolean> isValidBluemixVersion() {
		ResponseEntity<VersionInfo> versionInfo = restTemplate.getForEntity(BLUEMIX_URL, VersionInfo.class);
		if (HttpStatus.OK.equals(versionInfo.getStatusCode())) {
	       	return ResponseEntity.status(HttpStatus.OK).body(versionInfo.getBody() != null ?
	       			BLUEMIX_EXPECTEDVERSION.equalsIgnoreCase(versionInfo.getBody().getApiVersion()) : null);
		} 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }
	
}
