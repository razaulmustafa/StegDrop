import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;


import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class List_files {
private static final String ACCESS_TOKEN = "k2fSJjNwo7wAAAAAAAAQ99qNNFb-xPyJfl-n-oUe-CU2FK7MqhHrg_W2CI6BILWa";
	public void Cloud_view() throws DbxException, IOException {	
		
		// Create Dropbox client
	    DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
	    DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
	    // Get current account info
	    FullAccount account = client.users().getCurrentAccount();
	    System.out.println(account.getName().getDisplayName());
	    System.out.println(account.getName().getAbbreviatedName());
	    
	    System.out.println("\n");
	    System.out.println("*********************************");
	    System.out.println("\n");
	    
	    // Get files and folder metadata from Dropbox root directory
	   ListFolderResult result = client.files().listFolder("");
	    while (true) {
	        for (Metadata metadata : result.getEntries()) {
	            System.out.println(metadata.getPathLower());
	        }
	        if (!result.getHasMore()) {
	            break;
	        }
	        result = client.files().listFolderContinue(result.getCursor());
	    }  
		
	}
	
	public void storage_view() throws IOException{
		System.out.println("\nReading the whole file Manifest");
		System.out.println("\nContents of metadata.file");
    	InputStream input = new BufferedInputStream(new FileInputStream("Upload/metadata.file"));
    	byte[] buffer = new byte[8192];

    	try {
    	    for (int length = 0; (length = input.read(buffer)) != -1;) {
    	        System.out.write(buffer, 0, length);
    	    }
    	} finally {
    	    input.close();
    	}
    	System.out.println("\nContents of cloud_directories");
    	InputStream input1 = new BufferedInputStream(new FileInputStream("Upload/cloud_directories"));
    	byte[] buffer1 = new byte[8192];

    	try {
    	    for (int length = 0; (length = input1.read(buffer1)) != -1;) {
    	        System.out.write(buffer1, 0, length);
    	    }
    	} finally {
    	    input1.close();
    	}
    	System.out.println("\n\nContents of dummy_chunks_for_png");
    	InputStream input2 = new BufferedInputStream(new FileInputStream("Upload/dummy_chunks_for_png"));
    	byte[] buffer2 = new byte[8192];

    	try {
    	    for (int length = 0; (length = input2.read(buffer2)) != -1;) {
    	        System.out.write(buffer2, 0, length);
    	    }
    	} finally {
    	    input2.close();
    	}
    	System.out.println("\n\nContents of dummy_chunks.wav");
    	InputStream input21 = new BufferedInputStream(new FileInputStream("Upload/dummy_chunks.wav"));
    	byte[] buffer21 = new byte[8192];

    	try {
    	    for (int length = 0; (length = input21.read(buffer21)) != -1;) {
    	        System.out.write(buffer21, 0, length);
    	    }
    	} finally {
    	    input21.close();
    	}
    	
    	}
	
}
	
	

