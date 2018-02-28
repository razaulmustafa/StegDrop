import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Graphics;
import java.util.List;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import javax.imageio.ImageIO;
public class StegDrop_Upload {
private static final String ACCESS_TOKEN = "";

	public void upload() throws Exception{
		
		//Dropbox Connection
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
	   	DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
	   	System.out.println("Dropbox Client is instantiated");
	   	
	   	System.out.println("Wipping Metadata Files");
	   	FileWriter fwOb = new FileWriter("Upload/metadata.file", false); 
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
		
        FileWriter fwOb1 = new FileWriter("Upload/index_legi.file", false); 
        PrintWriter pwOb1 = new PrintWriter(fwOb1, false);
        pwOb1.flush();
        pwOb1.close();
        fwOb1.close();
		
        FileWriter fwOb2 = new FileWriter("Upload/index_legi.file", false); 
        PrintWriter pwOb2 = new PrintWriter(fwOb2, false);
        pwOb2.flush();
        pwOb2.close();
        fwOb2.close();
	   	
	   	
		String [] chunks = null;
    	String [] amended_cloud=null;
    	String [] amended_meta=null;
    	String [] amended_chunks=null;
		String key = "0123456789123456";
        File inputFile = new File("Upload/important.txt");
    	File encryptedFile = new File("Upload/test.encrypted");
        Cryptography object=new Cryptography ();
    	object.fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
    	File write = new File("Upload/metadata.file");
		FileWriter fileWriter = new FileWriter(write);
		fileWriter.write("**********File Metadata************\r\n");
		fileWriter.write(inputFile.getName()+"\r\n");
		fileWriter.write(key);
		fileWriter.write("\r\n");
		fileWriter.flush();
		fileWriter.close();
    	System.out.println("Encrypted file Object is generated!");
    	System.out.println("Metadata file is Updated");
        
    	/*
		//Hide encrypted File in the (.PNG) Media
        Steganography_image stega = new Steganography_image();
		//test encoding text and image
		BufferedImage original = stega.convertToBGR(ImageIO.read(new File("Upload/test.png")));
		//try encoding text in image
		byte[] messe= Files.readAllBytes(Paths.get("Upload/test.encrypted"));
		BufferedImage encodedText = stega.encodeText(original, messe);
		File stego_name=new File("Upload/stego_object.png");
		ImageIO.write(encodedText,"png", stego_name);
		File write_stego = new File("Upload/metadata.file");
		FileWriter fileWriter_meta = new FileWriter(write_stego,true);
		fileWriter_meta.write(stego_name.getName());
		fileWriter_meta.write("\r\n");
		fileWriter_meta.flush();
		fileWriter_meta.close();
		System.out.println("Stego Object is Created!");
		System.out.println("Metadata file is Updated");
	   	
		System.out.println("Invoking Read Solomon Encoder!!");
	   	ReedSolomon_Encoder encode_image=new ReedSolomon_Encoder();
        String [] stego_to_encode= {"Upload/stego_object.png"};
		encode_image.file_encoder(stego_to_encode);
		System.out.println("Encoded shards are created");
	    */
		
		//Hide cover media in the audio (.wav)
	    Steganography_audio audio_embed=new Steganography_audio();
    	String[] arguments_encode = {"##conceal", "Upload/cello.wav", "Upload/test.encrypted", "Upload/stego.wav"};
    	audio_embed.file_processor(arguments_encode);
    	System.out.println("Object is concealed in the audio cover");
    	System.out.println("Metadata file is Updated");
		
		
    	//Encode file with Reed_Solomon Encoder
    	System.out.println("Invoking Read Solomon Encoder!!");
        ReedSolomon_Encoder encode=new ReedSolomon_Encoder();
        String [] file_to_encode= {"Upload/stego.wav"};
		encode.file_encoder(file_to_encode);
		System.out.println("Encoded shards are created");
	    
	   	
		System.out.println("Reading File Manifest....");
		//read metadata file and cloud directories into the string array
	   	BufferedReader input = new BufferedReader(new FileReader("Upload/metadata.file"));
	   	String str;

	   	List<String> list = new ArrayList<String>();
	   	while((str = input.readLine()) != null){
	   	    list.add(str);
	   	}
        String[] metadata = list.toArray(new String[0]);
        
        //read cloud directories
        BufferedReader input_1 = new BufferedReader(new FileReader("Upload/cloud_directories"));
	   	String str_tmp="";

	   	List<String> directory_list = new ArrayList<String>();
	   	while((str_tmp = input_1.readLine()) != null){
	   	    directory_list.add(str_tmp);
	   	}
        String[] cloud = directory_list.toArray(new String[0]);
        
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
        
        int j=4,k=0,c=0,m=0, f=0;
        File index_legi = new File("Upload/index_legi.file");
        File index_dum = new File("Upload/index_dum.file");
	   	  while(j<10) {
	   		Random rand = new Random();
	   		int  n = rand.nextInt(10) + 1;
	   		if(n%2==0) {
            InputStream in1 = new FileInputStream("Upload/"+metadata[j]);
            FileMetadata upload = client.files().uploadBuilder(cloud[k]+"/"+metadata[j]).uploadAndFinish(in1);	
            FileWriter writer = new FileWriter(index_legi,true);
    		writer.write(String.valueOf(k)+"\t"+String.valueOf(j)+"\r\n");
    		writer.flush();
    		writer.close();
            j=j+1;
            k=k+1; 
	   		}
	   		else if(c<2){
	   		InputStream in1 = new FileInputStream("Upload/"+chunks[c]);
	        FileMetadata upload = client.files().uploadBuilder(cloud[k]+"/"+chunks[c]).uploadAndFinish(in1);	
	        FileWriter writer = new FileWriter(index_dum,true);
    		writer.write(String.valueOf(k)+"\t"+String.valueOf(c)+"\r\n");
    		writer.flush();
    		writer.close();
	        k=k+1; 
	        c=c+1;
	   		}
	   
	   	  }		
	   	
	}
	
}
    

