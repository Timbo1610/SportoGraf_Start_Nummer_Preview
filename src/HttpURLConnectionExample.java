

import org.codehaus.plexus.util.FileUtils;

import java.io.*;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";
    private File dir;


    static private int startnumber = 52857;
    private int stopnumber = 52870;

    public static void main(String[] args) throws Exception {


        HttpURLConnectionExample http = new HttpURLConnectionExample();

        http.readInput();
        http.runIterator();
    }

    public void readInput()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter Start:");
        try{
            try {
                startnumber = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }

        System.out.print("Enter End:");
        try{
            try {
                stopnumber = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
    }


    public void runIterator()
    {
        dir = checkDir("images");

        for(int i = startnumber; i < stopnumber ; i++)
        {
            try {
                System.out.println(i);
                String stringUrl = sendPost(i);
                if(!stringUrl.equals(""))
                {
                    URL url = new URL(stringUrl);

                    System.out.println(url);


                    File file = new File(dir + "/" + i +".jpg");

                    downloadImage(url,file);
                }
                else
                    System.out.println("No Images");



            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // HTTP POST request
    private String sendPost(int nummer) throws Exception {
        String result = "";
        String url = "https://www.sportograf.com/de/shop/search/4119";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "utf8=%E2%9C%93&authenticity_token=pR%2FHVjqN7YTCUmHz8%2FGt4tuqL4cJvdCx8DfKIicgKwE%3D&startnumber=" + nummer + "&commit=Suchen";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //ystem.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            if(inputLine.contains(".JPG") && inputLine.contains("startnumber")) {
                String[] parts = inputLine.split(",");
                for (String part:parts)
                {
                    if(part.contains(".JPG"))
                    {
                        result = part.split("\"")[3];
                        System.out.print(result);
                        break;
                    }
                }
                //System.out.println();
                break;

            }
        }
        in.close();

        //print result
        //System.out.println(response.toString());
        return result;
    }

    public void downloadImage(URL url,File file)
    {

        try {
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file.getAbsolutePath());

    }

    public File checkDir(String file)
    {
        File theDir = new File(file);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
        return theDir;
    }

}