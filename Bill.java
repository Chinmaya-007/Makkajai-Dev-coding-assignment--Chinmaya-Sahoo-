import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.*;
import java.text.DecimalFormat;

public class Bill {

	private ArrayList<Item> ItemsList = new ArrayList<Item>();
	private double total;
	private double taxTotal;
	
	@SuppressWarnings("resource")
	public Bill(String inputFileName){
				try {

            Scanner input = new Scanner(System.in);

            File file = new File(inputFileName);

            input = new Scanner(file);
            
            while (input.hasNextLine()) {
            	
            	String line = input.nextLine(); 
            	String[] words = line.split(" ");
            	int qty = Integer.parseInt(words[0]);
            	boolean isImported = line.contains("imported"); 
            	String[] exemptedItems =  new String[]{"book","chocolate","pills"}; 
            	int exemptedItemIndex = containsItemFromArray(line,exemptedItems); 
            	
            	String exemptedType = null;
            	
            	if(exemptedItemIndex != -1){
                	exemptedType = exemptedItems[exemptedItemIndex];
        			
            	}

            	int splitIndex = line.lastIndexOf("at");
            	
            	if(splitIndex == -1){
            		
            		System.out.println("Please Enter in correct format");
            		
            	} else {
            		
                	float price = Float.parseFloat((line.substring(splitIndex + 2))); 
                    
                	String name = line.substring(1, splitIndex); 
                	
                    for(int i = 0;i<qty;i++){
                    	
                    	Item newItem = null;
                    	
                    	if(isImported){

                        	if(exemptedType != null){
                        		
                        		if(exemptedType == "book"){
                        			newItem = new Item(name,price,ItemType.IMPORTED_BOOK);
                        		} else if(exemptedType == "pills"){
                        			newItem = new Item(name,price,ItemType.IMPORTED_MEDICAL);
                        		} else if(exemptedType == "chocolate"){
                        			newItem = new Item(name,price,ItemType.IMPORTED_FOOD);
                        		}

                        	} else {
                        		newItem = new Item(name,price,ItemType.IMPORTED_OTHERS);
                        	}
                        	
                    	} else {
                        	if(exemptedType != null){
                        		
                        		if(exemptedType == "book"){
                        			newItem = new Item(name,price,ItemType.BOOK);
                        		} else if(exemptedType == "pills"){
                        			newItem = new Item(name,price,ItemType.MEDICAL);
                        		} else if(exemptedType == "chocolate"){
                        			newItem = new Item(name,price,ItemType.FOOD);
                        		}

                        	} else {
                        		newItem = new Item(name,price,ItemType.OTHERS);
                        	}
                    	}
                    	
                        ItemsList.add(newItem); 
                    }
            	}
            	
            }
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	/** Prints out total bill
     * @param arg NULL
     * containing no command line argument
     * @Exception No Exception
     * @return no return value
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public void printBill(){
		int numOfItems = ItemsList.size();
		for(int i = 0;i<numOfItems;i++){
			System.out.println("1" + ItemsList.get(i).getName() + "at " + ItemsList.get(i).getPrice());
		}
		System.out.printf("Sales Tax: %.2f\n", taxTotal);
		System.out.println("Total: " + total);
	}

    /** sets out total Bill
     * @param arg NULL
     * containing no command line argument
     * @Exception No Exception
     * @return no return value
     * @functions
     * setTotal(args)
     * setSalesTaxTotal(args)
     * round(args)
     * roundTwoDecimals(args)
     * getTotal()
     * getSalesTaxTotal()
     * 
     * ----------------------------------------------------------------------------------------------------------------------------
     */
     
	
	public void totalBill(){
		int numOfItems = ItemsList.size();
		
		BigDecimal runningSum = new BigDecimal("0");
		BigDecimal runningTaxSum = new BigDecimal("0");
		
		for(int i = 0;i<numOfItems;i++){
			
			runningTaxSum = BigDecimal.valueOf(0);
			
			BigDecimal totalBeforeTax = new BigDecimal(String.valueOf(this.ItemsList.get(i).getPrice()));
			
			runningSum = runningSum.add(totalBeforeTax);
			
			if(ItemsList.get(i).isSalesTaxable()){
			
			    BigDecimal salesTaxPercent = new BigDecimal(".10");
			    BigDecimal salesTax = salesTaxPercent.multiply(totalBeforeTax);
			    
			    salesTax = round(salesTax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    runningTaxSum = runningTaxSum.add(salesTax);
			    
    
			} 
			
			if(ItemsList.get(i).isImportedTaxable()){

			    BigDecimal importTaxPercent = new BigDecimal(".05");
			    BigDecimal importTax = importTaxPercent.multiply(totalBeforeTax);
			    
			    importTax = round(importTax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    runningTaxSum = runningTaxSum.add(importTax);
			   
			}

			
			ItemsList.get(i).setPrice(runningTaxSum.floatValue() + ItemsList.get(i).getPrice());
		
			taxTotal += runningTaxSum.doubleValue();
			
			runningSum = runningSum.add(runningTaxSum);
		}
			taxTotal = roundTwoDecimals(taxTotal);
			total = runningSum.doubleValue();
	}

	/** sets out total value
     * @param arg amount BigDecimal
     * containing command line argument
     * @Exception No Exception
     * @return no return value
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public void setTotal(BigDecimal amount){
		total = amount.doubleValue();
	}
	
	/** return total
     * @param arg -NULL
     * containing no command line argument
     * @Exception No Exception
     * @return total
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public double getTotal(){
		return total;
	}
	
	/** sets out total tax value
     * @param arg amount BigDecimal
     * containing command line argument
     * @Exception No Exception
     * @return no return value
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public void setSalesTaxTotal(BigDecimal amount){
		taxTotal = amount.doubleValue();
	}
	
	/** return total tax
     * @param arg -NULL
     * containing no command line argument
     * @Exception No Exception
     * @return taxTotal
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public double getSalesTaxTotal(){
		return taxTotal;
	}
	
	public static int containsItemFromArray(String inputString, String[] items) {
		int index = -1;
		
		for(int i = 0;i<items.length;i++){
			
			index = inputString.indexOf(items[i]);

			if(index != -1)
				return i;
				
		}
		return -1;
		
	}
	
	/** return Result with round value
     * @param arg value, increment,RoundingMode
     * containing command line argument
     * @Exception No Exception
     * @return result
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public static BigDecimal round(BigDecimal value, BigDecimal increment,RoundingMode roundingMode) {
		if (increment.signum() == 0) {
		return value;
		} else {
			BigDecimal divided = value.divide(increment, 0, roundingMode);
			BigDecimal result = divided.multiply(increment);
			result.setScale(2, RoundingMode.UNNECESSARY);
			return result;
		}
	}
	
	/** return Result with two decimal round value
     * @param arg d
     * containing command line argument
     * @Exception No Exception
     * @return d
     * ----------------------------------------------------------------------------------------------------------------------------
     */
	public double roundTwoDecimals(double d) {
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    return Double.valueOf(twoDForm.format(d));
	}
	
	
	
}