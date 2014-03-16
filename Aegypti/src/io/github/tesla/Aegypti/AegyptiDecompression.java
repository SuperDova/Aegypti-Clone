package io.github.tesla.Aegypti;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class AegyptiDecompression {
	private URL jarURL;
	private JarURLConnection uc;
	private Manifest manifest;
	private Attributes attr;
	private JarFile jarF;
	private Enumeration<?> myEnum;
	public AegyptiDecompression(String jarPath) {
		try {
			jarURL = new URL("jar", "", "File://"+jarPath+"!/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			// Create a JarURLConnection to the jar
			uc = (JarURLConnection)jarURL.openConnection();
		} catch (IOException e) {
			// If that fails print an error and stack trace
			System.err.println("[!] Failed to open jar file!");
			e.printStackTrace();
			// Exit
			System.exit(0);
		}
		try {
			// Get the manifest from the jar
			manifest = uc.getManifest();
		} catch (IOException e1) {
			// If that fails print a stack trace
			e1.printStackTrace();
		}
		// Get the attributes from the JarURLConnection
		attr = manifest.getMainAttributes();
		try {
			// Try to instantiate it
			jarF = new JarFile(jarPath);
		} catch (IOException e) {
			// If that fails print a stack trace and exit
			e.printStackTrace();
			System.exit(0);
		}
		myEnum = jarF.entries();
	}
	public void decompress() {
		// Tell the user we're making a tmp directory and then make one
		System.out.println("[*] Creating temporary directory. . .");
		File f = new File("tmp");
		try{
			// If we correctly create the director
		    if(f.mkdir()) { 
		    	// Tell the user we made it
		        System.out.println("[*] Directory Created");
		    } else {
		    	// Otherwise we say we didn't :P
		        System.out.println("[*] Directory is not created");
		    }
		} catch(Exception e){
			// If we catch any exception print the stack trace
		    e.printStackTrace();
		}
		// Tell the user we will decompress the jar file
		System.out.println("[*] Decompressing jar file. . .");
		// While there are more jar file objects
		while (myEnum.hasMoreElements()) {
			// Get the file object
			JarEntry file = (JarEntry) myEnum.nextElement();
			// Print that we are unpacking the file
			System.out.println("[*] Unpacking "+file.getName()+". . .");
			// Get the file object
			
			
			File f1 = new File("tmp" + File.separator + file.getName());
			System.out.println(file.getSize());
			System.out.println(f1.getAbsolutePath());
			//System.out.println();
			if (file.isDirectory()) {
				// If it's a directory make the directory
		        f1.mkdir();
		        // Continue :P
		        continue;
		    }
			// InputStream object
			InputStream is = null;
			try {
				// Instantiate it
				is = jarF.getInputStream(file);
			} catch (IOException e) {
				// If that fails print a stack trace
				e.printStackTrace();
			}
			// FileOutputStream object
			//FileOutputStream fos = null;
			

			BufferedWriter writer = null;
			try {
				// Try to instantiate it with the file
				writer = new BufferedWriter(new FileWriter(f1.getAbsolutePath()));
			} catch (Exception e) {
				// If the file is not found tell the user
				System.out.println("[!] "+f1.getAbsolutePath()+" file not found!");
				e.printStackTrace();
			}
			try {
				// While the input stream is available
				while (is.available() > 0) {
					// Write to the output stream
				    writer.write(is.read());
				}
			} catch (IOException e) {
				// If we can't do that print a stack trace
				e.printStackTrace();
			}
			try {
				// Close the output stream
				writer.flush();
			    writer.close();
			} catch (IOException e) {
				// If there is an IOException, print the stack trace
				e.printStackTrace();
			}
			try {
				// Try to close it
				is.close();
			} catch (IOException e) {
				// If that fails print the stack trace
				e.printStackTrace();
			}
		}
	}
	public Manifest getManifest() { return manifest; }
	public Attributes getAttributes() { return attr; }	
}