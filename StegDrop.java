import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.dropbox.core.DbxException;

public class StegDrop {
public static void main(String args[]) throws Exception{

	 // assume input
	System.out.println("Select an appropriate option");
	Scanner scan=new Scanner(System.in);
	String input = scan.nextLine();
    String value=input.valueOf(input);
	switch(value) {
    case "upload":
    StegDrop_Upload s =new StegDrop_Upload();s.upload();  
        break;
    case "download":
    StegDrop_Download b =new StegDrop_Download();b.download(); 
    	break;
    case "list":
    	List_files a=new List_files();
    	//a.Cloud_view();
    	a.storage_view();
        break;
    case "delete":
    	StegDrop_Delete d=new StegDrop_Delete();d.delete();
        break;
    // etc...
        default:
        System.out.println("Invalid Input");
}
}
}