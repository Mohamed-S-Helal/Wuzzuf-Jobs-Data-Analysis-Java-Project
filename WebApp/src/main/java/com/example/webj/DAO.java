package com.example.webj;


import org.knowm.xchart.*;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DAO {
    private Table df;
    private Table clean;

    public DAO() {
        //1. Read data set and convert it to dataframe.
        this.df = null;
        try {
            Table tbl = Table.read().csv("Wuzzuf_Jobs.csv");
            this.df = tbl;
            this.clean = tbl.dropRowsWithMissingValues().dropDuplicateRows();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Table getDf() {
        return df;
    }

    public Table getClean() {
        return clean;
    }

    public Table getSample() {
        return df.sampleN(10);
    }

    public Table getStructure() {
        return df.structure();
    }

    public Table getSummary() {
        return df.summary().transpose(true,true);
    }

    public Table getTopCompanies() {
        return clean.stringColumn("Company").countByCategory().sortDescendingOn("Count").first(10);
    }

    public Table getTopTitles() {
        return clean.stringColumn("Title").countByCategory().sortDescendingOn("Count").first(10);
    }

    public Table getTopAreas() {
        return clean.stringColumn("Location").countByCategory().sortDescendingOn("Count").first(10);
    }

    public Table getTopSkills() {
        List<String> skills = clean.stringColumn("Skills").asList().stream().flatMap(skill-> Arrays.stream(skill.split(","))).collect(Collectors.toList());
        StringColumn SkillsCol = StringColumn.create("All Skills" , skills);
        return SkillsCol.countByCategory().sortDescendingOn("Count").first(10);
    }

    public Table factorizeYearsExp() {
        Pattern p1 = Pattern.compile("(\\d+)");
        Pattern p2 = Pattern.compile("(\\d+)\\W+(\\d+)");

        IntColumn minYears = IntColumn.create("MinYears");
        IntColumn maxYears = IntColumn.create("MaxYears");

        StringColumn col = clean.stringColumn("YearsExp");
        for (String s : col){
            Matcher m1 = p1.matcher(s);
            Matcher m2 = p2.matcher(s);
            if (m1.find()){
                minYears.append(Integer.parseInt(m1.group(1)));
                if (m2.find()){
                    maxYears.append(Integer.parseInt(m2.group(2)));
                }else {
                    maxYears.append(25);
                }
            }else {
                minYears.append(0);
                maxYears.append(0);
            }
        }
        return clean.addColumns(minYears, maxYears);

    }

    List<HashMap> dfToJson(Table t){
        List<HashMap> jsonList = new ArrayList();
        List<String> keys = t.columnNames();
        int size = t.columnCount();
        Iterator<Row> rows = t.stream().iterator();
        while (rows.hasNext()){
            Row values = rows.next();
            HashMap row = new HashMap<>();
            for (int i=0; i<size; i++){
                row.put(keys.get(i), values.getObject(i));
            }
            jsonList.add(row);

        }
        return jsonList;
    }

    public String drawChart(Table t, String chartType) {
        int n = t.rowCount();
        String title = t.name();
        if (chartType.equals("pie")) {
            PieChart c = new PieChartBuilder().title(title).build();
            c.getStyler().setHasAnnotations(true);
            for (int i = 0; i < n; i++) {
                c.addSeries((String) t.get(i, 0), (Integer) t.get(i, 1));
            }
            try {
                BitmapEncoder.saveJPGWithQuality(c, "target/classes/static./chart.jpg", 1f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            CategoryChart c =new CategoryChartBuilder().build();
            c.addSeries(title,t.stringColumn(0).asList(),t.intColumn(1).asList());
            c.getStyler().setHasAnnotations(true);
            try {
                BitmapEncoder.saveJPGWithQuality(c, "target/classes/static./chart.jpg", 1f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<img src=\"chart.jpg\"/>\n" +
                "</body>\n" +
                "</html>";
    }

}
