package com.theironyard.javawithclojure.jhporter;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

        public static final String FILE_LOCATION = "people.txt";
        public static final int NAMES_PER_PAGE = 20;
        static int currentPage = 1;
        static ArrayList<Person> pagePeeps;
        static boolean firstpage = true;
        static boolean lastpage = false;


    public static void main(String[] args) throws FileNotFoundException
    {
        ArrayList<Person> people = loadPeople(FILE_LOCATION);
        double numOfEntries = people.size();
        int totalPages = (int)Math.ceil(numOfEntries/NAMES_PER_PAGE);
        Spark.staticFileLocation("/public");

        System.out.println();

        Spark.get(
                "/",
                (request, response) ->
                {
                    pagePeeps = new ArrayList<>();
                    if (totalPages>currentPage)
                    {
                        for (int i = ((currentPage - 1) * NAMES_PER_PAGE); i < (currentPage * NAMES_PER_PAGE); i++)
                        {
                            pagePeeps.add(people.get(i));
                        }
                    }
                    else
                    {
                        for (int i = ((currentPage - 1) * NAMES_PER_PAGE); i < people.size(); i++)
                        {
                            pagePeeps.add(people.get(i));
                        }
                    }
                    HashMap m = new HashMap();
                    m.put("pagepeeps", pagePeeps);
                    m.put("firstpage", firstpage);
                    m.put("lastpage", lastpage);
                    m.put("pagenumber",currentPage);
                    m.put("totalpages", totalPages);


                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                (request, response) ->
                {
                    String idStr = request.queryParams("id");
                    int identity = -1;
                    Person individual = null;

                    if (idStr != null)
                    {
                        identity = Integer.valueOf(idStr);
                    }
                    if (identity!= -1)
                    {
                        individual = people.get(identity);
                    }
                    HashMap h = new HashMap();
                    if (individual !=null)
                    {
                        h.put("first_name", individual.first_name);
                        h.put("last_name", individual.last_name);
                        h.put("email", individual.email);
                        h.put("country", individual.country);
                        h.put("ip_address", individual.ip_address);

                    }
                    return new ModelAndView(h, "person.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
            "/next-page",
            (request, response ) ->
            {
                currentPage++;
                if (currentPage == 1)
                {
                    firstpage = true;
                }
                else
                {
                    firstpage= false;
                }

                if (currentPage == totalPages)
                {
                    lastpage = true;
                }
                else
                {
                    lastpage = false;
                }
                response.redirect("/");
                return "";
            }
        );
        Spark.post(
                "/previous-page",
                (request, response ) ->
                {
                    currentPage--;
                    if (currentPage == 1)
                    {
                        firstpage = true;
                    }
                    else
                    {
                        firstpage= false;
                    }

                    if (currentPage == totalPages)
                    {
                        lastpage = true;
                    }
                    else
                    {
                        lastpage= false;
                    }

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/select-page",
                (request, response ) ->
                {
                    int chosenPage=currentPage;
                    String pageStr = request.queryParams("pageselected");
                    if (pageStr.isEmpty() || Integer.valueOf(pageStr)>totalPages ||Integer.valueOf(pageStr)<1)
                    {

                    }
                    else
                    {
                        chosenPage = Integer.valueOf(pageStr);
                        currentPage = chosenPage;
                    }
                    if (currentPage == 1)
                    {
                        firstpage = true;
                    }
                    else
                    {
                        firstpage= false;
                    }

                    if (currentPage == totalPages)
                    {
                        lastpage = true;
                    }
                    else
                    {
                        lastpage= false;
                    }
                    response.redirect("/");
                    return "";
                }
        );
    }



    //int id, String first_name, String last_name, String email, String country, String ip_address
    public static ArrayList<Person> loadPeople(String filename) throws FileNotFoundException {
        ArrayList<Person> people = new ArrayList<>();
        File f = new File(filename);
        Scanner fileScanner =  new Scanner(f);
        fileScanner.nextLine();
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
