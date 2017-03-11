package com.gable.test;

import rx.Observable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppMain {
    public static void main(String[] args) {
        EmployeeRepository repository = new EmployeeRepository();


    }
}

class EmployeeRepository {
    public Observable<Employee> list() {
        return Observable.just(
                new Employee(1L, "user1"),
                new Employee(2L, "user2"),
                new Employee(3L, "user3")
        );
    }
}

class AddressRepository {
    private Map<Long, List<Address>> addresses = new HashMap<>();

    public AddressRepository() {
        addresses.put(1L, Arrays.asList(
                new Address(1L, "34/432", "add1 of emp1"),
                new Address(1L, "432/42", "add2 of emp1")));
        addresses.put(2L, Arrays.asList(
                new Address(2L, "34/67", "add1 of emp2")));
        addresses.put(3L, Arrays.asList(
                new Address(3L, "754/34", "add1 of emp3"),
                new Address(3L, "55/63", "add2 of emp3"),
                new Address(3L, "536/63", "add3 of emp3")));
    }

    public Observable<Address> getByEmployeeId(Long employeeId) {
        return Observable.from(addresses.get(employeeId));
    }
}

class Employee {
    private Long id;
    private String name;

    public Employee(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class Address {
    private Long employeeId;
    private String no;
    private String road;

    public Address(Long employeeId, String no, String road) {
        this.employeeId = employeeId;
        this.no = no;
        this.road = road;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getNo() {
        return no;
    }

    public String getRoad() {
        return road;
    }
}