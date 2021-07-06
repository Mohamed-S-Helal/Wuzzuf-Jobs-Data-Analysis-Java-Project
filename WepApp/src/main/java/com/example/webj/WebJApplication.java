package com.example.webj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.tablesaw.api.Table;

import java.util.HashMap;
import java.util.List;

@SpringBootApplication
@RestController
public class WebJApplication {
    DAO d = new DAO();

    public static void main(String[] args) {
        SpringApplication.run(WebJApplication.class, args);
    }

    @RequestMapping("/sample")
    List sample(){
        return d.dfToJson(d.getSample());
    }

    @RequestMapping("/clean")
    List clean(){
        return d.dfToJson(d.getClean());
    }

    @RequestMapping("/structure")
    List structure(){
        return d.dfToJson(d.getStructure());
    }

    @RequestMapping("/summary")
    List summery(){
        return d.dfToJson(d.getSummary());
    }

    @RequestMapping("/top/companies")
    List getTopCompanies(){
        return d.dfToJson(d.getTopCompanies());
    }

    @RequestMapping("/top/titles")
    List getTopTitles(){
        return d.dfToJson(d.getTopTitles());
    }

    @RequestMapping("/top/areas")
    List getTopAreas() {
        DAO d = new DAO();
        return d.dfToJson(d.getTopAreas());
    }

    @RequestMapping("/top/skills")
    List getTopSkills() {
        DAO d = new DAO();
        return d.dfToJson(d.getTopSkills());
    }

    @RequestMapping("/factorize/years")
    List factorizeYearsExp() {
        DAO d = new DAO();
        return d.dfToJson(d.factorizeYearsExp());
    }

    @RequestMapping("/companies.chart")
    String topCompaniesChart() {
        DAO d = new DAO();
        return d.drawChart(d.getTopCompanies(), "pie");
    }

    @RequestMapping("/titles.chart")
    String topTitlesChart() {
        DAO d = new DAO();
        return d.drawChart(d.getTopTitles(), "bar");
    }

    @RequestMapping("/areas.chart")
    String topAreasChart() {
        DAO d = new DAO();
        return d.drawChart(d.getTopAreas(), "bar");
    }

}


