import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;

public class StegDrop_Delete{
	private static final String ACCESS_TOKEN = "";
	
	public void delete() throws Exception{
	
		String [] chunks = null;
		
		BufferedReader meta = new BufferedReader(new FileReader("Upload/metadata.file"));
	   	String str;
	   	List<String> list = new ArrayList<String>();
	   	while((str = meta.readLine()) != null){
	   	    list.add(str);
	   	}
	    String[] metadata = list.toArray(new String[0]);
	    
		System.out.println("Please enter the name of file");
		Scanner scan=new Scanner(System.in);
		String input = scan.nextLine();
		if (input.equals(metadata[1])) {
		System.out.println("File "+input+ " exists in the metadata file.");	
		}
		else {
		System.out.println("File doesn't exist");
	    System.exit(0);
		}
	   //read the list of cloud directories.
	   BufferedReader input_1 = new BufferedReader(new FileReader("Upload/cloud_directories"));
	   String str_tmp="";
	   List<String> directory_list = new ArrayList<String>();
	   while((str_tmp = input_1.readLine()) != null){
		directory_list.add(str_tmp);
		 } String[] cloud = directory_list.toArray(new String[0]);
		//read the list of dummy chunks.
	    if(metadata[3].equals("stego.wav")) {
	        //read dummy_chunks file.
	        BufferedReader input_2 = new BufferedReader(new FileReader("Upload/dummy_chunks.wav"));
		   	String str_tmp_1="";

		   	List<String> dummy_chunks = new ArrayList<String>();
		   	while((str_tmp_1 = input_2.readLine()) != null){
		   	dummy_chunks.add(str_tmp_1);
		   	}
		   	chunks=dummy_chunks.toArray(new String[0]);
	        }     
	        if(metadata[3].equals("stego_object.png")) {
	            //read dummy_chunks file.
	            BufferedReader input_2 = new BufferedReader(new FileReader("Upload/dummy_chunks_for_png"));
	    	   	String str_tmp_1="";

	    	   	List<String> dummy_chunks = new ArrayList<String>();
	    	   	while((str_tmp_1 = input_2.readLine()) != null){
	    	   	    dummy_chunks.add(str_tmp_1);
	    	   	}
	         chunks=dummy_chunks.toArray(new String[0]);
	            }
	    //read the index matrix for legitimate chunks    
		Scanner scanner = new Scanner(new File("Upload/index_legi.file"));
		ArrayList<String> index = new ArrayList<String>();
		while (scanner.hasNext()) {
		    String[] columns = scanner.nextLine().split("/t");
		    String data = columns[columns.length-1];
		    index.add(data);
		    System.out.println(data);    
		}
		int size = index.size();
		String[] cloud0 = new String[size];  
		String[] met0 = new String[size];
		for(int i=0;i<size;i++)
		{
		    String[] temp = index.get(i).split("\t");
		    cloud0[i] = temp[0];
		    met0[i] = temp[1];
		}
		//read the index matrix for dummy chunks.
		Scanner scanner_2 = new Scanner(new File("Upload/index_dum.file"));
		ArrayList<String> index_dum = new ArrayList<String>();
		while (scanner_2.hasNext()) {
		    String[] columns_2 = scanner_2.nextLine().split("/t");
		    String data_2 = columns_2[columns_2.length-1];
		    index_dum.add(data_2);
		    System.out.println(data_2);
		}
		int size_2 = index_dum.size();
		String[] cloud1 = new String[size_2];  
		String[] met1 = new String[size_2];
		for(int i=0;i<size_2;i++)
		{
		    String[] temp_1 = index_dum.get(i).split("\t");
		    cloud1[i] = temp_1[0];
		    met1[i] = temp_1[1];
		} 	
		//start downloading data chunks.
 DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
 DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		  	int j=0,k=0,m=0,p=0,c=0;
		  	
		   	  while(j<7|c<2) { 
		   		Random rand = new Random();
		   		int  n = rand.nextInt(10) + 1;
		   		if(n%2==0) {
		   		DeleteResult delete = client.files().deleteV2(cloud[Integer.valueOf(cloud0[k])]+"/"+metadata[Integer.valueOf(met0[j])]);
		   	    j=j+1;
		   	    k=k+1;
		   	    if(j==6)break;
		   		}
		   		if(c<2){
		   		DeleteResult delete = client.files().deleteV2(cloud[Integer.valueOf(cloud1[m])]+"/"+chunks[Integer.valueOf(met1[c])]);
		        m=m+1; 
		        c=c+1;
		   		}
		   	  }
		   	  
		   	File a=new File("Upload/metadata.file"); 
	        a.delete();
	        File b=new File("Upload/index_dum.file"); 
	        b.delete();
	        File f=new File("Upload/index_legi.file"); 
	        f.delete();
  	        System.out.println("Objects are successfully deleted and metadatafile is updated\n");
}
}
