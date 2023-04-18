package controller.httprequests;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPRequest {


    /**
     * Make a generic GET request
     * @param queryUrl The query target url
     * @param query The query url suffix
     * @return
     */
    public String genericJsonQuery(String queryUrl, String query){

        String jsonReturn;

        try {

            URL url = new URL(queryUrl + query);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestProperty("Content-Type", "*/*");
            jsonReturn = streamToString(http.getInputStream());
            http.disconnect();
            return jsonReturn;
        }
        catch(Exception e){
            System.out.println(e.getMessage() + e.getClass());
        }

        return null;
    }

    /**
     * Make a POST request with a body
     *
     * @param queryUrl The query target url
     * @param query The query body
     * @return The response body as a String
     */
    public String genericJsonPostQuery(String queryUrl, String query){

        String jsonReturn;

        try {

            URL url = new URL(queryUrl);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            byte[] out = query.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            jsonReturn = streamToString(http.getInputStream());
            http.disconnect();
            return jsonReturn;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    /**
     * Helper function to change the HTTP response bodies into strings
     * @param is InputStream returned by the HTTP requests
     * @return The read stream as a string
     */
    public String streamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
