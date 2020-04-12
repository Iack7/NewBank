package newbank.server.database;

import java.util.HashMap;
import java.util.Map;


public class CurrencyDB {
	  private Map<String, Double> currencies; //contains the name of the currencies and the exchange rate to USD
	  
	  public CurrencyDB() {
		  currencies = new HashMap<>();

		  //Initialize
		  currencies.put("USD", 1.0);		  
		  currencies.put("GBP", 1.22);		  
		  currencies.put("EUR", 1.10);		  
		  currencies.put("CNY", 0.14);		  
	  }
	  
	  public double convertCurrency(double amount, String fromCurrency, String toCurrency) throws Exception {
		  if (! currencies.containsKey(fromCurrency)) {
			  throw new Exception("The currency does not exists.");			  
		  }
		  if (! currencies.containsKey(toCurrency)) {
			  throw new Exception("The currency does not exists.");			  
		  }		  
		  
		  double conversionFactor = currencies.get(fromCurrency);
		  double amountUSD = amount/conversionFactor;
		  
		  conversionFactor = currencies.get(toCurrency);
		  double amountTarget = amountUSD*conversionFactor;
		  
		  return amountTarget;
	  }

}