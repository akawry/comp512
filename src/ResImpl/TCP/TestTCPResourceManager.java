package ResImpl.TCP;

import java.util.Scanner;

public class TestTCPResourceManager extends AbstractTCPResourceManager {

	@Override
	public String processInput(String line) {
		System.out.println("Got command: " + line);
		return "generic response";
	}

	public static void main(String[] args) {
		new Thread(new Runnable(){
			public void run() {
				TestTCPResourceManager a = new TestTCPResourceManager();
				a.listen(2020);				
			}
			
		}).start();
		
		
		TestTCPResourceManager b = new TestTCPResourceManager();
		Scanner scan = new Scanner(System.in);
		String line;
		while (true){
			line = scan.nextLine();
			b.send(line, "localhost", 2020);
		}
		
		
	}
	
	

}
