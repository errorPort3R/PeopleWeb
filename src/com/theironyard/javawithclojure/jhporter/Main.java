package com.theironyard.javawithclojure.jhporter;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public Main() {
    }
        public static final String FILE_LOCATION = "people.txt";

    public static void main(String[] args) throws FileNotFoundException
    {

        ArrayList<Person> people = loadPeople(FILE_LOCATION);

        Spark.get(
                "/",
                (request, response ) ->
                {
                    String idStr = request.queryParams("replyId");
                    int replyId = -1;
                    if (!idStr.equals(null))
                    {
                        replyId = Integer.valueOf(idStr);
                    }
                    ArrayList <Message> subset = new ArrayList<Message>();
                    for (Message msg : messages)
                    {
                        if (msg.replyId == replyId)
                        {
                            subset.add(msg);
                        }
                    }
                    HashMap m = new HashMap();
                    m.put("messages", subset);
                    m.put("username", username);
                    m.put("replyId", replyId);
                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );

    }


    //String id, String first_name, String last_name, String email, String country, String ip_address
    public static ArrayList<Person> loadPeople(String filename) throws FileNotFoundException {
        ArrayList<Person> people = new ArrayList<>();
        File f = new File(filename);
        Scanner fileScanner =  new Scanner(f);
        while (fileScanner.hasNext())
        {
            String line = fileScanner.nextLine();
            String[] fields = line.split(",");
            Person person = new Person(Integer.valueOf(fields[0]), fields[1], fields[2], fields[3], fields[4], fields[5]);
            people.add(person);
        }
        return people;
    }
}
