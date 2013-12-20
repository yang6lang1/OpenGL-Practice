package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TXTReader {

  private String filename;
  private BufferedReader reader;
  
  public TXTReader(String filename) throws FileNotFoundException{
	this.filename = filename;
	reader = new BufferedReader(new FileReader(filename));
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