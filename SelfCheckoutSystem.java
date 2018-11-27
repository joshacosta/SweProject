import java.util.Scanner;
import java.util.*;
import java.util.concurrent.TimeUnit;
//INTERFACES

interface CheckOutInterface{
	void updateInventory(String item);
	void promptScan(ArrayList<Item> inventory);
	void cancel();
}

interface CardReaderInterface{
	void readCardInfo(CardInfo info);
	
}

interface BankInterface{
	void getPIN(CardInfo card);
}

interface TimerInterface{
	//sendRequestToPrintAtMidnight(); x2 ?
}  

interface CustPrinterInterface{
	void printReceipt(TransactionLog currentOrder, Subtotal subTotal);
}

interface ItemScannerInterface{
	void displayItemDesc(int index);
	void promptAlcoholCheck();
	void getSubtotal(Subtotal subTotal);
	void displayTotalPrice(Subtotal subTotal);
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




		while(true) {
			System.out.print("Welcome! Enter any key to begin scanning: ");
			response = kb.next();
			if(!response.equals("")) {
				scan.promptScan(inventory);
			}
			System.out.println("Returning to welcome screen... (updating inventory)");
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

	public void updateInventory(String item) {
		
	}

	public void promptScan(ArrayList<Item> inventory) {
		Checkout mainControl = new Checkout(inventory);
		mainControl.scan();
		
	}

	public void cancel() {
		CancelCheckOut c = new CancelCheckOut();
		c.cancel();
	}


}



//ENTITIES

class CardInfo{
	int cardNum;
	String expDate;
	int securityCode;
	int PIN;
	
	public CardInfo(int cardNum, String expDate, int securityCode, int PIN) {
		this.cardNum = cardNum;
		this.expDate = expDate;
		this.securityCode = securityCode;
		this.PIN = PIN;
	}
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
	
	public TransactionLog() {
		AmountSold.put("Grapes", 0);
		AmountSold.put("Beef", 0);
		AmountSold.put("Bread", 0);
		AmountSold.put("Milk", 0);
		AmountSold.put("Beer", 0);
	}
	
	public void updateOrder(String itemName,double price) {
		this.TotalRevenue += price;
		AmountSold.put(itemName, AmountSold.get(itemName)+1);
		
	}
	
	public void getSubtotal() {
		System.out.println("SUBTOTAL: $"+ this.TotalRevenue);
	}
}

class Inventory{
	//Array of all item objects --> Item[] allItems;
	//Object --> someItem Item; //refer to Item for object data
	
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
		this.tax = this.totalPrice*.0825;
	}
	
	public void getTotal() {
		double total = this.totalPrice + this.tax;
		String tot = String.format("%.2f", total);
		System.out.println("TOTAL: $"+tot);
	}
}

//CONTROLS

class Checkout implements  ItemScannerInterface, CustPrinterInterface{
	ArrayList<Item> inventory =new ArrayList<Item>();
	
	public Checkout(ArrayList<Item> inventory){
		this.inventory = inventory;
	}
	
	public void updateInventory(String item) {
		System.out.println("update");
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
			
			if(response.equals("7")) {
				displayTotalPrice(subTotal);
				printReceipt(currentOrder, subTotal);
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
					
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateInventory("yeet");
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
		ContainsAlcohol check = new ContainsAlcohol();
		check.check(this.inventory.get(4).containsAlcohol);
	}


	@Override
	public void displayTotalPrice(Subtotal subTotal) {
		subTotal.getTotal();
		PaymentMethod pay = new PaymentMethod(subTotal);
	}

	@Override
	public void printReceipt(TransactionLog currentOrder, Subtotal subTotal) {
		CardInfo info = new CardInfo(123456789, "01/22", 123, 1234); //card information

		subTotal.getSubtotal();
		subTotal.getTotal();

		
		
	}


	
}

class Restock{
	
}

class ChangeProductInfo{
	
}

//BUSINESS LOGICS

class ContainsAlcohol{
	public void check(Boolean hasAlcohol) {
		if(hasAlcohol) {
			System.out.println("Are you 21?... It seems you check out.");
			System.out.println("-Cashier enters confirmation code-");
		}
	}
}

class CancelCheckOut{
	public void cancel() {
		System.out.println("Order has been cancelled...");
		System.exit(0);
	}
	
}

class PaymentMethod  {
	
	
	public PaymentMethod(Subtotal subTotal) {
		Scanner kb = new Scanner(System.in);
		System.out.println("How would you like to pay? \n1) Cash\n2) Card");
		System.out.print("Choose: ");
		String response = kb.next();
		
		if(response.equals("2")) {
			promptReadCardInfo();
		}else if(response.equals("1")) {
			promptReadCash(subTotal);
		}
		
	}

	
	public void promptReadCardInfo() {
		PayByCard card  = new PayByCard();
		System.out.println("Signing receipt...");
		System.out.println("\nYour receipt:\n");
		CardInfo info = new CardInfo(123456789, "01/22", 123, 1234);
		System.out.println("Card Number: " + info.cardNum%10000 +"\nAuthorization: 98764321");
	}
	public void promptReadCash(Subtotal subTotal){
		PayByCash cash = new PayByCash(subTotal);
		System.out.println("\nYour receipt:\n");
		//print receipt
	}

}

//APPLICATION LOGICS

class PayByCard implements CardReaderInterface, BankInterface{
	public PayByCard() {
		System.out.println("-Card is entered...-");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Boolean isCardValid = true; //we shall assume this for sake of flow of program
		CardInfo info = new CardInfo(123456789, "01/22", 123, 1234); //card information is read
		if(isCardValid) {
			readCardInfo(info);//card is read
			getPIN(info);
		}
		else {
			System.out.println("Card not accepted... cancelling order");
			System.exit(0);
		}
		
		
		
		
	}

	@Override
	public void readCardInfo(CardInfo info) {
		if(info.cardNum > 0) { //this assumed rule for specific customer card
			System.out.println("Card has been read...");
		}
		
	}

	@Override
	public void getPIN(CardInfo info) {
		Scanner kb = new Scanner(System.in);
		System.out.print("Please enter your four digit PIN number...\t (hint: its 1234): "); //assume customer knows their PIN so disregard the "hint"
		int PIN = kb.nextInt();
		
		AuthorizationCenter auth = new AuthorizationCenter(PIN);
		if(auth.requestValidation()) {
			auth.getAuthNum();
		}
		
	}

	
	
}

class PayByCash{

	public PayByCash(Subtotal subTotal) {
		Scanner kb = new Scanner(System.in);
		System.out.print("Press any key to enter your cash: ");
		String res = kb.next();
		if(!res.equals("")) {
			System.out.print("How much cash will you be entering?: ");
			double cash = kb.nextDouble();
			CheckCash checkCash = new CheckCash(subTotal, cash);
			if(checkCash.check()) {
				double change = cash - subTotal.totalPrice;
				System.out.println("Cash has been received.\nYou are receiving $" + change + " in change...");
			}else {
				System.out.println("You didn't enter enough cash... cancelling order.");
				System.exit(0);
			}
		}
	}
}

class AuthorizationCenter{
	int PIN;
	
	public AuthorizationCenter(int PIN) {
		this.PIN = PIN;
	}
	
	public boolean requestValidation() {
		if(this.PIN == 1234) {
			return true;
		}else {
			return false;
		}
	}
	
	public void getAuthNum() {
		System.out.println("Your card has been accepted.\nYour authorization number is 987654321.");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class CheckCash{
	Subtotal subTotal;
	double cash;
	
	public CheckCash(Subtotal subTotal, double cash) {
		this.subTotal = subTotal;
		this.cash = cash;
	}
	
	public boolean check() {
		if(this.subTotal.totalPrice - this.cash <0) {
			return true;
		}
		else
			return false;
	}
}

class PrintDailyReport{
	
}


class PrintInventory{
	
}

//////////////////////




