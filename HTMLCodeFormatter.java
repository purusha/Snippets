package com.skillbill.at.integration;

import java.util.Objects;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import lombok.Data;

public class HTMLCodeFormatter {
    
    /*

        pom.xml dependencies:        

        <dependency>
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-client</artifactId>
            <version>1.19.2</version>
        </dependency>
        <dependency>
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-json</artifactId>
            <version>1.19.2</version>
        </dependency>            
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.8.3</version>
        </dependency>
 
     */

    final static String BODY = "<html><body><p>Ciao,</p> <div>guarda il <a href=\"#\">video</a></div></body></html>";

    final static Client CLIENT;

    final static String SID = generateS();
    final static String FID = generateS();
    final static String SEED = generateI();

    static {
        final ClientConfig cc = new DefaultClientConfig();
        cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        cc.getSingletons().add(new JacksonJsonProvider());

        CLIENT = Client.create(cc);
        CLIENT.addFilter(new GZIPContentEncodingFilter() {});
        CLIENT.addFilter(new LoggingFilter(System.out));
    }

    public static void main(String[] args) throws InterruptedException {

        final ClientResponse post = builder("convert", BODY);
        System.out.println("first stage complete with status = " + post.getStatus());
        
        Thread.sleep(150);
        System.out.println("\n\n");

        final ClientResponse get = builder("status", null);
        System.out.println("second stage complete with status = " + get.getStatus());
        
        Result entity = get.getEntity(Result.class);
        System.out.println(entity.getResult());

    }

    private static ClientResponse builder(String action, String body) {
        final Cookie c1 = new Cookie("_pk_id.125.19a9", "f013b42acfe3d04d.1524600501.1.1524600502.1524600501.");
        final Cookie c2 = new Cookie("_pk_ses.125.19a9", "*");

        WebResource stage = CLIENT
            .resource("http://htmlformatter.com/" + action + "/" + SID + "/" + FID)
            .queryParam("rnd", SEED);

        if (!Objects.isNull(body)) {
            stage = stage.queryParam("code", body);
        }

        Builder builder = stage
            .header("Origin", "http://htmlformatter.com")
            .header("Referer", "http://htmlformatter.com/")
            .header("X-Requested-With", "XMLHttpRequest")
            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
            .acceptLanguage("it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7")            
            .accept(MediaType.APPLICATION_JSON)
            .cookie(c1)
            .cookie(c2);

        if (Objects.isNull(body)) {
            return builder
                .get(ClientResponse.class);
        } else {
            return builder
                .type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(ClientResponse.class);
        }
    }

    private static String generateI() {
        return "0." + RandomStringUtils.randomNumeric(16);
    }

    private static String generateS() {
        return RandomStringUtils.randomAlphanumeric(16);
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    static class Result {
        private String result;
        private String status;
    }
}
