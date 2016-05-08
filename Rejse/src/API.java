import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

public class API {


	private final String API = "http://ec2-52-50-239-39.eu-west-1.compute.amazonaws.com:8080/rejseafregningssystem/webapi/";
	private Scanner keyboard;
	private int answer = 0;
	private int telefon, takst;
	private String brugernavn, password, land, yn;
	private boolean fortsæt = true;

	public API()
	{
		keyboard = new Scanner(System.in);
		
		brugernavn = null;
		password = null;

		while(fortsæt){

			System.out.println("Hvis du vil se brugerinformation, tast 1.");
			System.out.println("Hvis du vil opdatere dit telefonnummer, tast 2.");
			System.out.println("Hvis du vil tilføje et land, tast 3");
			System.out.println("Hvis du vil have en liste over alle lande og takster, tast 4");
			answer = keyboard.nextInt();
			if(answer != 1 && answer != 2 && answer != 3)
			{
				System.out.println("Du skal indtaste enten 1, 2 eller 3!");
				answer = keyboard.nextInt();
			}
			if(answer == 1)
			{
				if(brugernavn == null || password == null)
				{
					System.out.println("Indtast brugernavn:");
					brugernavn = keyboard.next();
					System.out.println("Indtast dit password:");
					password = keyboard.next();
				}

				try {
					String output = getUrl(API + "info/" + brugernavn + "/" + password);
					System.out.println(output);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(answer == 2)
			{
				if(brugernavn == null || password == null)
				{
					System.out.println("Indtast brugernavn:");
					brugernavn = keyboard.next();
					System.out.println("Indtast dit password:");
					password = keyboard.next();
				}

				System.out.println("Indtast dit nye telefonnummer");
				telefon = keyboard.nextInt();

				try {
					String urlParameters = "telefon/" + brugernavn + "/" + password + "/" + telefon;
					String output = putUrl(API + urlParameters, urlParameters);
					System.out.println(output);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(answer == 3)
			{
				if(brugernavn == null || password == null)
				{
					System.out.println("Indtast brugernavn:");
					brugernavn = keyboard.next();
					System.out.println("Indtast dit password:");
					password = keyboard.next();
				}

				System.out.println("Indtast et nyt land:");
				land = keyboard.next();
				System.out.println("Indtast dagpengetaksten for landet:");
				takst = keyboard.nextInt();

				try {
					String urlParameters = "land/" + brugernavn + "/" + password + "/" + land + "/" + takst;
					String output = postUrl(API + urlParameters, urlParameters);
					System.out.println(output);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("Vil du fortsætte? y/n");
			yn = keyboard.next();
			if(yn.equals("n"))
			{
				fortsæt = false;
			}

		}


	}


	//Henter indholdet fra en url og omskriver det til en string
	public String getUrl(String url) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
		StringBuilder sb = new StringBuilder();
		String linje = br.readLine();
		while (linje != null) {
			sb.append(linje + "\n");
			linje = br.readLine();
		}
		return sb.toString();
	}

	public String postUrl(String url, String urlParameters) throws IOException {
		String resp = null;

		URL newURL = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		String requestBody = urlParameters.toString();
		byte[] outputBytes = requestBody.getBytes();
		OutputStream os = conn.getOutputStream();
		os.write(outputBytes);


		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		StringBuilder responseOutput = new StringBuilder();

		while((line = br.readLine()) != null)
		{
			responseOutput.append(line);
		}

		resp = responseOutput.toString();

		os.close();



		return resp;
	}

	public String putUrl(String url, String urlParameters) throws IOException 
	{
		String resp = null;

		URL newURL = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		String requestBody = urlParameters.toString();
		byte[] outputBytes = requestBody.getBytes();
		OutputStream os = conn.getOutputStream();
		os.write(outputBytes);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		StringBuilder responseOutput = new StringBuilder();

		while((line = br.readLine()) != null)
		{
			responseOutput.append(line);
		}

		resp = responseOutput.toString();

		os.close();

		return resp;
	}

}
