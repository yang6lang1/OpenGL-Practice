package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class TXTReader {

  private String filename;
  private BufferedReader reader;
  
  public TXTReader(String filename) throws FileNotFoundException, URISyntaxException{
	this.filename = filename;
	Log.p(""+getClass().getResource(filename).toString());
	reader = new BufferedReader(new FileReader(new File(getClass().getResource(filename).toURI())));
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