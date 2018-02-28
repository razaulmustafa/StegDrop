import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;



public class Steganography_image {
	
	private final int MESSAGE_LENGTH_BITS = 32;  // max bits used to encode length of message 
	private final int WIDTH_BITS = 16;			 // max bits used to encode length of image width 
	private final int HEIGHT_BITS = 16; 		 // max bits used to encode length of image height
	
	/**Converts an image to 3 bytes of blue, green, red each, respectively
	 * @param bimg the bufferedimage to be converted to
	 * @return new bufferedimage with color scheme (BGR) desired
	 */
	BufferedImage convertToBGR(BufferedImage bimg) {
		BufferedImage newImage = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = newImage.getGraphics();
		g.drawImage(bimg, 0, 0, null);
		g.dispose();
		return newImage;
	}
	
	/**
	 * Hides the bytes in an image by grabbing the LSB in image byte and 
	 * giving it the value of the corresponding bit.
	 * @param bimg the image where the text will be hidden in
	 * @param bytes the message/image to hide
	 * @param offset how much offset there is in bimg array
	 */
	private void hideBytes(byte[] bimg, byte[] bytes, int offset) {
		for (int i = 0; i < bytes.length; i++) {
			int character = bytes[i];
			for (int j = 7; j >= 0; j--) {  // 8 bits per character
				// get corresponding bit
				int bit = (character >> j) & 1;
				
				// get last image byte, set last bit to 0 ( AND 0xFE) then OR with bit
				bimg[offset] = (byte) ((bimg[offset] & 0xFE) | bit);
				offset++;
			}
		}
	}
	
	/**
	 * Gets the bytes of an image in form of array
	 * @param bimg the image to get byte array from
	 * @return byte array of bufferedimage
	 */
	private byte[] getImageBytes(BufferedImage bimg) {
		WritableRaster raster = bimg.getRaster();
		DataBufferByte dbb = (DataBufferByte) raster.getDataBuffer();
		return dbb.getData();
	}
	
	/**
	 * Gets the bytes of an integer and stores them in a byte array
	 * @param size the size of the byte array
	 * @param number which integer to convert to bytes
	 * @return byte array with number in bytes
	 */
	private byte[] getBytesFromInt(int size, int number) {
		byte[] b = new byte[size];
		
		for (int i = size-1, shift = 0; i >= 0; i--, shift += 8) 
			b[i] = (byte) ((number & (0x000000FF << shift)) >> shift);
		
		return b;
	}
	
	
	
	
	/**
	 * Encodes bytes into an image using the least significant bit (LSB) algorithm
	 * @param bimg the image used to hide text in
	 * @param message the text to be hidden
	 * @return new image with the text embedded in it
	 */
	public BufferedImage encodeText(BufferedImage bimg, byte[] message) {
		byte[] bytes = message;
		byte[] messageLengthArray = getBytesFromInt(MESSAGE_LENGTH_BITS/8, bytes.length); // 8 bits per byte
		byte[] imageArray = getImageBytes(bimg);
		
		// hide bytes in image
		hideBytes(imageArray, messageLengthArray, 0);
		hideBytes(imageArray, bytes, MESSAGE_LENGTH_BITS);
				
		return bimg;
	}

	
	/**
	 * Gets the hidden text from an image using LSB algorithm
	 * @param bimg the image with the hidden text inside
	 * @return the hidden text
	 */
	public byte[] decodeText(BufferedImage bimg) {
		byte[] imageArray = getImageBytes(bimg);
		
		int length = 0;
		
		// get length stored in messgae_length_bits
		for (int i = 0; i < MESSAGE_LENGTH_BITS; i++) {
			length = (length << 1) | (imageArray[i] & 1);
		}
		
		byte[] result = new byte[length];
		int index = MESSAGE_LENGTH_BITS; // skip bits for length of message

		// loop til length of message and grab each LSB from image
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < 8; j++) { // each bit
				// shift by 1 and grab last bit of next image byte
				result[i] = (byte) ((result[i] << 1) | (imageArray[index] & 1));
				index++;
			}
		}
		
			
		return result;
	}
	
	
	

}