public class Main {

	public static void main(String[] args) {
	    
		int i;
		// if you want to make any changes to the test cases then plese change them in the test files
		String a[]={"test1.txt","test2.txt","test3.txt"};
		
		for(i=0;i<3;i++)
		{
		    Bill b = new Bill(a[i]);	
    		b.totalBill();
    		System.out.println("Output "+i);
    		b.printBill();
    		System.out.println();
		}
		
	}

}