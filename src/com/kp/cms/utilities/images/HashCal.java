package com.kp.cms.utilities.images;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.*;
public class HashCal
{

/*public static void main(String[] args) 
{

try{

	String merchant_key="JBZaLc";
	String salt="GQs7yium";
	String action1 ="";
	String base_url="https://test.payu.in";
	int error=0;
	String hashString="";
	
 

	
	Enumeration paramNames = request.getParameterNames();
	Map<String,String> params= new HashMap<String,String>();
    	while(paramNames.hasMoreElements()) 
	{
      		String paramName = (String)paramNames.nextElement();
      
      		String paramValue = request.getParameter(paramName);

		params.put(paramName,paramValue);
	}
	String txnid ="";
	if(empty(params.get("txnid"))){
		Random rand = new Random();
		String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
		txnid=hashCal("SHA-256",rndm).substring(0,20);
	}
	else
		txnid=params.get("txnid");
        udf2 = txnid;
	String txn="abcd";
	String hash="";
	String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
	if(empty(params.get("hash")) && params.size()>0)
	{
		if( empty(params.get("key"))
			|| empty(params.get("txnid"))
			|| empty(params.get("amount"))
			|| empty(params.get("firstname"))
			|| empty(params.get("email"))
			|| empty(params.get("phone"))
			|| empty(params.get("productinfo"))
			|| empty(params.get("surl"))
			|| empty(params.get("furl"))
			|| empty(params.get("service_provider"))
	)
			
			error=1;
		else{
			String[] hashVarSeq=hashSequence.split("\\|");
			
			for(String part : hashVarSeq)
			{
				hashString= (empty(params.get(part)))?hashString.concat(""):hashString.concat(params.get(part));
				hashString=hashString.concat("|");
			}
			hashString=hashString.concat(salt);
			

			 hash=hashCal("SHA-512",hashString);
			action1=base_url.concat("/_payment");
		}
	}
	else if(!empty(params.get("hash")))
	{
		hash=params.get("hash");
		action1=base_url.concat("/_payment");
	}






}catch(Exception e){
	e.printStackTrace();
}


}//main


public static boolean empty(String s)
{
	if(s== null || s.trim().equals(""))
		return true;
	else
		return false;
}


public static String hashCal(String type,String str){
	byte[] hashseq=str.getBytes();
	StringBuffer hexString = new StringBuffer();
	try{
	MessageDigest algorithm = MessageDigest.getInstance(type);
	algorithm.reset();
	algorithm.update(hashseq);
	byte messageDigest[] = algorithm.digest();
        
	

	for (int i=0;i<messageDigest.length;i++) {
		String hex=Integer.toHexString(0xFF & messageDigest[i]);
		if(hex.length()==1) hexString.append("0");
		hexString.append(hex);
	}
		
	}catch(NoSuchAlgorithmException nsae){ }
	
	return hexString.toString();


}*/

}
