package ua.nure.nosyk.courseproject.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="database")
public class DAOConfig {
    private String type = "MYSQL";
    private String url;
    private String user;
    private String password;
    public String getType(){return type;} public void setType(String v){type=v;}
    public String getUrl(){return url;} public void setUrl(String v){url=v;}
    public String getUser(){return user;} public void setUser(String v){user=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
}
