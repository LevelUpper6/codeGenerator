package codeGenerator;

import com.google.common.base.CaseFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

public class App {

    private static final String dataBase = "unrepaired";    // 数据库名
    private static final String username = "root";          // 数据库用户名
    private static final String password = "123456";        // 数据库密码
    private static final String tableName = "unrepaired";   // 表名
    private static final String pack = "main.java.codeGenerator.model";// 存放位置
    private static final List<String> sysBaseField = Arrays.asList("id", "createTime", "updateTime", "creatorId", "updaterId", "siteNum", "deleted");

    private static final String what = "model.ftl";// model, dto, query, results. todo: 选择模版文件,来生成不同的javaBean

    public static void main(String... args) {

        try {
            // 获取数据
            Collection<Map<String, String>> properties = readData(dataBase, username, password, tableName);
//            System.out.println("....." + "properties = " + properties);

            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File("template"));
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            //获取模板文件
            Template template = cfg.getTemplate(what);

            Map<String, Object> map = new HashMap<>();
            map.put("tableName", tableName);//表名
            map.put("className", getClassName(tableName));//类名:表名的驼峰式写法
            map.put("pack", pack);//包名
            map.put("properties", properties);//字段

            // 生成输出到控制台
            System.out.println("//===================================================================================");
            Writer out = new OutputStreamWriter(System.out);
            template.process(map, out);
            out.flush();
            System.out.println("\n//=================================================================================");

            //生成输出到文件
            String root = genPackStr("C:\\evolution\\codeGenerator\\src", pack);
            File fileDir = new File(root);
            // 创建文件夹，不存在则创建
            FileUtils.forceMkdir(fileDir);
            // 指定生成输出的文件
            File output = new File(fileDir + "/" + getClassName(tableName) + ".java");
            Writer writer = new FileWriter(output);
            template.process(map, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取表数据
     */
    private static Collection<Map<String, String>> readData(String dataBase, String username, String password, String tableName) {
        Collection<Map<String, String>> properties =
                new HashSet<>();
        Connection conn = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.10.6:3306/" + dataBase + "?useUnicode=true&characterEncoding=UTF-8", username, password);
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getColumns(null, null, tableName, null);
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                String columnName = rs.getString("COLUMN_NAME");
                String fieldName = genFieldName(columnName);
                String fieldType = genFieldType(rs.getString("TYPE_NAME"));
                String fieldRemarks = rs.getString("REMARKS");
                if (! sysBaseField.contains(fieldName)) { // 排除继承的维护字段.
                    map.put("columnName", columnName);//字段名字
                    map.put("fieldName", fieldName);//驼峰格式
                    map.put("fieldType", fieldType);//java类型
                    map.put("fieldRemarks", fieldRemarks);//字段注释
                    properties.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return properties;
    }

    /**
     * 根据包名获取对应的路径名
     */
    private static String genPackStr(String root, String pack) {
        StringBuilder result = new StringBuilder(root);
        String[] dirs = pack.split("\\.");
        for (String dir : dirs) {
            result.append("/").append(dir);
        }
        return result.toString();
    }

    private static String getClassName(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);//guava转化为驼峰命名法(首字母大写)
    }

    /**
     * 根据表字段名获取java中的字段名
     */
    private static String genFieldName(String field) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field);//guava转化为驼峰命名法(首字母小写)
    }

    /**
     * 根据表字段的类型生成对应的java的属性类型
     */
    private static String genFieldType(String type) {
        String result = "String"; //包含其他类型比如text
        if (type.toLowerCase().equals("varchar")) {
            result = "String";
        } else if (type.toLowerCase().equals("int")) {
            result = "Integer";
        } else if (type.toLowerCase().equals("datetime")) {
            result = "Date";
        }
        return result;
    }

}
