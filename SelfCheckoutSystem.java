import java.util.*;
import java.util.concurrent.TimeUnit;
//INTERFACES

interface CheckOutInterface{
	void updateInventory(String item);
	void promptScan(ArrayList<Item> inventory);
	void cancel();
}

interface CardReaderInterface{
	//useCard();
	//readCardInfo();
	//getPIN();
}

interface BankInterface{
	//verifyCardInfo(); or requestValidation();
}

interface TimerInterface{
	//sendRequestToPrintAtMidnight(); x2 ?
}

interface CustPrinterInterface{
	//printReceipt();
	//displayWelcome();
}

interface ItemScannerInterface{
	void displayItemDesc(int index);
	void promptAlcoholCheck();
	void getSubtotal(Subtotal subTotal);
	void displayTotalPrice();
}

interface WorkerInterface{
	//updateInventory();
	//addNewItem();
}

public class SelfCheckoutSystem implements CheckOutInterface{
	public static void main(String[] args) {
		SelfCheckoutSystem scan = new SelfCheckoutSystem();
		Scanner kb = new Scanner(System.in);
		String response;
		
		//create inventory
		
		ArrayList<Item> inventory =new ArrayList<Item>();
		
		//Items
		Item Grapes = new Item(12345, "Grapes" , 2.99, "A bag of grapes..." , "No discounts", 50, false);
		Item Beef = new Item(23451, "Beef" , 9.99, "A slab of beef..." , "No discounts", 50, false);
		Item Bread = new Item(34512, "Bread" , 4.99, "A loaf of bread..." , "No discounts", 50, false);
		Item Milk = new Item(45123, "Milk" , 3.99, "A gallon of milk..." , "No discounts", 50, false);
		Item Beer = new Item(12345, "Beer" , 12.99, "A pack of beer..." , "No discounts",50, true);
		inventory.add(Grapes);
		inventory.add(Beef);
		inventory.add(Bread);
		inventory.add(Milk);
		inventory.add(Beer);
		//Inventory mainInventory = new Inventory (inventory);


		
		
		System.out.print("Welcome! Enter any key to begin scanning: ");
		response = kb.next();
		if(!response.equals("")) {
			scan.promptScan(inventory);
		}
	}

	//public void updateInventory(String value, int decValue, ArrayList<Item> inventory) {
		//inventory.remove(value);
		//printList(inventory);
	//}
	
	//public void printList(ArrayList<Item> inventory) {
		//Arrays.toString(inventory.toArray());
	//}

	public void promptScan(ArrayList<Item> inventory) {
		Checkout mainControl = new Checkout(inventory);
		mainControl.scan();
		
	}

	public void cancel() {
		CancelCheckOut c = new CancelCheckOut();
		c.cancel();
	}

	@Override
	public void updateInventory(String item) {
		// TODO Auto-generated method stub
		
	}


}



//ENTITIES

class CardInfo{
	int cardNum;
	String expDate;
	int securityCode;
	int PIN;
	int bankAuthNum;
}

class Item{
	int itemCode;
	String itemName;
	double price;
	String description;
	String discountInfo;
	int quantity;
	Boolean containsAlcohol;
	
	public Item(int itemCode, String itemName, double price, String description, String discountInfo, int quantity, Boolean containsAlcohol) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.price = price;
		this.description = description;
		this.discountInfo = discountInfo;
		this.containsAlcohol = containsAlcohol;
	}
}

class TransactionLog{
	Map<String,Integer> AmountSold = new HashMap<String, Integer>();
	double TotalRevenue = 0.0;
	
	public void updateProductInfo(String oldKey, String newKey, int newValue) {
		if (newValue == 222) {
			newValue = AmountSold.get(oldKey);
		}
		
		AmountSold.remove(oldKey);
		AmountSold.put(newKey, newValue);
		System.out.println(Arrays.asList(AmountSold));
		
		//SelfCheckoutSystem scs = new SelfCheckoutSystem();
		//scs.updateInventory(oldKey, newValue);
	}
	
	public void promptDailyReport() {
		System.out.println(Arrays.asList(AmountSold));
	}
	
	public TransactionLog() {
		AmountSold.put("Grapes", 0);
		AmountSold.put("Beef", 0);
		AmountSold.put("Bread", 0);
		AmountSold.put("Milk", 0);
		AmountSold.put("Beer", 0);
	}
	
	public void updateProductInfo(String newKey) {
		AmountSold.remove("Grape");
		AmountSold.put(newKey, 0);
	}
	
	public void updateOrder(String itemName,double price) {
		this.TotalRevenue += price;
		AmountSold.put(itemName, AmountSold.get(itemName)+1);
		
	}
	
	public void getKeys() {
		
	}
	
	public void getSubtotal() {
		System.out.println("SUBTOTAL: $"+ this.TotalRevenue);
	}
}

class Inventory{
	//Array of all item objects --> Item[] allItems;
	//Object --> someItem Item; //refer to Item for object data
	
}

class NewItem{
	String description;
	float price;
	String discountInfo;
	int quantity; // should also be apart of Item?
	int inventoryLevel; // ? wut dis meen
}

class Changes{
	long targetItemCode;
	int quantity;
}

class Subtotal{
	double totalPrice;
	double tax;
	
	public Subtotal(double totalPrice, double tax) {
		this.totalPrice = totalPrice;
		this.tax = tax;
	}
	
	public void getSubtotal() {
		System.out.println("SUBTOTAL: $"+this.totalPrice);
		String tax = String.format("%.2f", this.totalPrice*.0825);
		System.out.println("TAX: $"+tax);
	}
	
	public void updateSubtotal(double price) {
		this.totalPrice += price;
	}
}

//CONTROLS

class Checkout implements  ItemScannerInterface, CustPrinterInterface{
	ArrayList<Item> inventory =new ArrayList<Item>();
	
	public Checkout(ArrayList<Item> inventory){
		this.inventory = inventory;
	}
	
	public void updateInventory(String item) {
		
	}

	public void scan() {
		Scanner kb = new Scanner(System.in);
		TransactionLog currentOrder = new TransactionLog();
		TransactionLog completeLog = new TransactionLog();
		Subtotal subTotal = new Subtotal(0.0,0.0);
		
		while (true) {
			System.out.println("Scan items: \t Actions:\n1) Grapes \t 6) Check subtotal\n2) Beef \t 7) Finish (TOTAL)\n3) Bread \t 8) Cancel \n4) Milk \n5) Beer \t (For employee access, enter code)\n");
			System.out.print("Choose an action: ");
			String response = kb.nextLine();
			System.out.println(currentOrder);	
			
			if(response.equals("7")) {
				break;
			}
			
			switch(response) {
				case "1":
					displayItemDesc(1);
					updateSubtotal(currentOrder,1, completeLog);
					updateSubtotal(subTotal,2.99);
					break;
				case "2":
					displayItemDesc(2);
					updateSubtotal(currentOrder,2, completeLog);
					updateSubtotal(subTotal,9.99);
					break;
				case "3":
					displayItemDesc(3);
					updateSubtotal(currentOrder,3, completeLog);
					updateSubtotal(subTotal,4.99);
					break;
				case "4":
					displayItemDesc(4);
					updateSubtotal(currentOrder,4, completeLog);
					updateSubtotal(subTotal,3.99);
					break;
				case "5":
					promptAlcoholCheck();
					displayItemDesc(5);
					updateSubtotal(currentOrder,5, completeLog);
					updateSubtotal(subTotal,12.99);
					break;
				case "6":
					getSubtotal(subTotal);
					break;
				case "8":
					SelfCheckoutSystem c = new SelfCheckoutSystem();
					c.cancel();
					break;
				case "123":
					ChangeProductInfo cpi = new ChangeProductInfo();
					cpi.Change();
					//System.out.println("What is the new name for this item? ");
					//String newKey = kb.nextLine();
					//cpi.Change(newKey);
					//PrintDailyReport pdr = new PrintDailyReport();
					//pdr.Print();
					
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}

	
	public void updateSubtotal(TransactionLog currentOrder,int index, TransactionLog completeLog) {
		currentOrder.updateOrder(this.inventory.get(index-1).itemName ,this.inventory.get(index-1).price);
		
	}
	
	public void updateSubtotal(Subtotal subTotal, double price) {
		subTotal.updateSubtotal(price);
	}
	
	public void getSubtotal(Subtotal subTotal) {
		subTotal.getSubtotal();
	}

	@Override
	public void displayItemDesc(int index) {
		System.out.println(this.inventory.get(index-1).description +"($"+ this.inventory.get(index-1).price +")");
		
	}

	@Override
	public void promptAlcoholCheck() {
		if(this.inventory.get(4).containsAlcohol) {
			System.out.println("Are you 21?... It seems you check out.");
			System.out.println("-Cashier enters confirmation code-");
		}
		
	}


	@Override
	public void displayTotalPrice() {
		// TODO Auto-generated method stub
		
	}
	
}

class Restock{
	
}

class ChangeProductInfo{
	Scanner kb = new Scanner(System.in);
	public void Change() {
		TransactionLog completeLog = new TransactionLog();
		System.out.print("Which product would you like to change? ");
		String oldKey = kb.nextLine();
		System.out.print("Replace it with what? ");
		String newKey = kb.nextLine();
		System.out.print("New value for above key?(if you dont want to change it put 222) ");
		int newValue = kb.nextInt();
		completeLog.updateProductInfo(oldKey, newKey, newValue);
	}
}

//BUSINESS LOGICS

class ContainsAlcohol{
	
}

class CancelCheckOut{
	public void cancel() {
		System.out.println("Order has been cancelled...");
		System.exit(0);
	}
	
}

class PaymentMethod{
	
}

//APPLICATION LOGICS

class PayByCard{
	
}

class PayByCash{
	
}

class AuthorizationCenter{
	
}

class CheckCash{
	
}

//class PrintDailyReport{
	//public void Print() {
		//TransactionLog currentLog = new TransactionLog();
		//currentLog.change();
	//}
//}


class PrintInventory{
	
}

//////////////////////




