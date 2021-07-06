package com.example.tester;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Scanner;

@Component
public class CallRestService implements CommandLineRunner {
    public static void call() {
        RestTemplate t = new RestTemplate();
        while (true) {
            Scanner sc = new Scanner(System.in);  // Create a Scanner object

            System.out.println("Enter url");
            String nxtLine = sc.nextLine();
            if (!nxtLine.equals("close")) {
                String url = nxtLine;  // Read user input
                List l = t.getForObject(url, List.class);
                System.out.println(l);
            }else{
                sc.close();
                break;
            }

        }
    }
    @Override
    public void run(String... args) throws Exception {
        call();
    }
}
