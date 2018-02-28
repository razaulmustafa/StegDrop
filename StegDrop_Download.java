import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

public class StegDrop_Download {
private static final String ACCESS_TOKEN = "";

public static void download() throws Exception{
	
	String [] chunks = null;
	System.out.println("Reading the file Manifest...");
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
	System.out.println("Reading cloud directories");
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
    	System.out.println("Reading dummy chunks catalogue");
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
    System.out.println("Reading indexes of directories from 2D Matrix.");
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
	System.out.println("Download Chunks Randomly");
	/*
   DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
  	DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
  	int j=0,k=0,m=0,p=0,c=0;
  	
   	  while(j<7|c<2) { 
   		Random rand = new Random();
   		int  n = rand.nextInt(10) + 1;
   		if(n%2==0) {
   		FileOutputStream downloadck1 = new FileOutputStream("Download/" + metadata[Integer.valueOf(met0[j])]);
   		FileMetadata Chunk1 = client.files().downloadBuilder(cloud[Integer.valueOf(cloud0[k])]+"/"+metadata[Integer.valueOf(met0[j])]).download(downloadck1);
   	    j=j+1;
   	    k=k+1;
   	    if(j==6)break;
   		}
   		if(c<2){
   		FileOutputStream downloadck1 = new FileOutputStream("Download/"+chunks[Integer.valueOf(met1[c])]);
   	    FileMetadata Chunk1 = client.files().downloadBuilder(cloud[Integer.valueOf(cloud1[m])]+"/"+chunks[Integer.valueOf(met1[c])]).download(downloadck1);		
        m=m+1; 
        c=c+1;
   		}
   	  } 
   	*/
   	if(metadata[3].equals("stego.wav")) {  
	//decode data audio chunks or recover, if any of them is missing.
   	System.out.println("Audio chunks are successfully downloaded\n"); 	  	
    ReedSolomon_Decoder decoder=new ReedSolomon_Decoder();
    String [] file_to_decode= {"Download/stego.wav"};
    decoder.file_decoder(file_to_decode);
    System.out.println("Audio data shards are successfully decoded\n");
    //extract encrypted object from the audio file.
    
    System.out.println("Extracting Hidden object from audio cover\n");
	Steganography_audio audio_extract=new Steganography_audio();
	String[] arguments_decode = {"##extract", "Download/stego.wav.decoded", "Download/extracted.encrypted"};
	audio_extract.file_processor(arguments_decode);
	System.out.println("Object is successfully extracted\n");
   	}
   	
   	if(metadata[3].equals("stego_object.png")) { 
   	System.out.println("Image chunks are successfully downloaded\n"); 	  	
   	ReedSolomon_Decoder decoder=new ReedSolomon_Decoder();
   	String [] file_to_decode= {"Download/stego_object.png"};
   	decoder.file_decoder(file_to_decode);	
   	System.out.println("Image data shards are successfully decoded\n");
   	
   	System.out.println("Extracting Hidden object from image cover\n");
   	Steganography_image decode_image=new Steganography_image();
   	byte[] image=decode_image.decodeText(ImageIO.read(new File("Download/stego_object.png.decoded")));
   	FileOutputStream fos = new FileOutputStream("Download/extracted.encrypted");
   	fos.write(image);
   	fos.close();
   	System.out.println("Object is successfully extracted\n");
   	
   	}
   	
	File decryptedFile = new File("Download/important.txt");
   	Cryptography object=new Cryptography ();
   	String key = metadata[2];
   	File encryptedFile = new File("Download/extracted.encrypted");
   	object.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
   	System.out.println("File is successfuly decrypted!!!\nLocate important.txt in the Download directory\n");
   	System.out.println("*********************************");
   	
   }
}
