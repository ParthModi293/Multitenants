package com.demo.multitenants.multitanants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee save(){
        Employee newEmployee = new Employee();
        newEmployee.setName("Baeldung");
        return employeeRepository.save(newEmployee);
    }

}
