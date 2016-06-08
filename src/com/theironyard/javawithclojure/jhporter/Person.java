package com.theironyard.javawithclojure.jhporter;

/**
 * Created by jeffryporter on 6/8/16.
 */
public class Person
{
    int id;
    String first_name;
    String last_name;
    String email;
    String country;
    String ip_address;

    public Person(int id, String first_name, String last_name, String email, String country, String ip_address) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.country = country;
        this.ip_address = ip_address;
    }
}
