package com.gable.test;

import org.javatuples.Pair;
import rx.Observable;
import rx.Subscriber;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AppMain {
    public static void main(String[] args) throws InterruptedException {
        /*EmployeeService service = new EmployeeService();
        service.getAllEmployee()
                .subscribe(
                        pair -> System.out.println(pair.getValue0().getName() + " " + pair.getValue1().size())
                );*/

        CountDownLatch latch = new CountDownLatch(1);

        Observable<String> obs1 = Observable.interval(3, TimeUnit.MILLISECONDS)
                .take(10)
                .map(l -> "Obs1_" + l);
        Observable<String> obs2 = Observable.interval(1, TimeUnit.MILLISECONDS)
                .take(20)
                .map(l -> "Obs2_" + l);



        Observable<String> concatObs = Observable.concat(obs1, obs2);
        Observable<String> mergeObs = Observable.merge(obs1, obs2);

        mergeObs.subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> latch.countDown());
        latch.await();
    }
}

class EmployeeService {
    private EmployeeRepository employeeRepository = new EmployeeRepository();
    private AddressRepository addressRepository = new AddressRepository();

    public Observable<Pair<Employee, List<Address>>> getAllEmployee() {
        return employeeRepository.list()
                .doOnNext(emp -> System.out.println("do on employee name " + emp.getName()))
                .filter(employee -> employee.getId() == 2L)
                .flatMap(
                        employee -> addressRepository.getByEmployeeId(employee.getId())
                                .toList()
                                .map(addesses -> Pair.with(employee, addesses))
                );
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