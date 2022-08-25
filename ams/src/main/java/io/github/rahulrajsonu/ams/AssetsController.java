package io.github.rahulrajsonu.ams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/assets")
public class AssetsController {

    @GetMapping
    public ResponseEntity<Asset> getAssetDetails(@RequestParam String id){
        RestTemplate rt = new RestTemplate();
        try {
            if(id.equals("BT202201")) {
                ResponseEntity<Employee> response = rt.getForEntity("http://localhost:8080/api/employee?id=1002", Employee.class);
                return ResponseEntity.ok(new Asset("BT202201", "Lenovo Laptop", response.getBody()));
            }else {
                ResponseEntity<Employee> response = rt.getForEntity("http://localhost:8080/api/employee?id=1001", Employee.class);
                return ResponseEntity.ok(new Asset("BT202202", "Lenovo Laptop", response.getBody()));
            }
        } catch (Exception e){
            throw e;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class Asset {
        String id;
        String name;
        Employee assignedTo;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class Employee {
        String id;
        String name;
    }
}
