package io.github.rahulrajsonu.eim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @GetMapping(value = "/api/employee",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployee(@RequestParam String id){
        if(id==null){
            throw new RuntimeException("Id is required.");
        }
        if("1002".equals(id)){
            return ResponseEntity.ok(new Employee("1002", "Rahul Raj"));
        }else {
            throw new NotFoundException("Employee not found: "+id);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class Employee {
        String id;
        String name;
    }
}
