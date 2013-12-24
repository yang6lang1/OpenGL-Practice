package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Text reader: save to be used for project deployment.
 * */
public class TXTReader {
  
  private String filename;
  private BufferedReader reader;
  
  public TXTReader(String filename) throws FileNotFoundException{
	this.filename = filename;
	InputStream in = getClass().getResourceAsStream(filename);
	reader = new BufferedReader(new InputStreamReader(in));
  }
  
  public String getInputAsOneString() throws IOException{
	String output = "";
	String textline = "";
	while((textline = reader.readLine()) != null){
	  output += textline;
	}
	return output;
  }

  public String getInputAsOneStringRetainLineBreaker() throws IOException{
	String output = "";
	String textline = "";
	while((textline = reader.readLine()) != null){
	  output += textline+"\n";
	}
	return output;
  }
  
  public String getFilename() {
	return filename;
  }

  public void setFilename(String filename) {
	this.filename = filename;
  }
}