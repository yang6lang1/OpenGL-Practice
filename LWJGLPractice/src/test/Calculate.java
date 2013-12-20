package test;

public class Calculate {
  
  public String input = "371+227+493+453+16+16+14+31+396+242+57+58+298+198+970+989+109+211+349+54+101+117+126+40+"
  +"412+198+39+38+70+175+64+143+34+91+93+91+151+134+32+396+804+273+222+5+9+1500+87+53+34+135+63+73+141+146+342+129+"
  +"50+38+211+158+56+78+19+109+54+38+12+22+27+86";
  
  public String input2 = "15+15";
  
  public Calculate(){
	calculate();
  }
  
  private void calculate() {
	String[] tokens = input.split("\\+");
	int[] nums = new int[tokens.length];
	int sum = 0;
	for(int i = 0; i < tokens.length; i++){
	  try{
		nums[i] =  Integer.parseInt(tokens[i]);
		sum += nums[i];
	  }
	  catch(Exception e){
		System.out.println(e.getMessage());
	  }
	}
	System.out.println("totoal written code: "+ sum + " lines. Good work!");
  }

  public static void main(String[] args){
	Calculate calculate = new Calculate();
  }
}
