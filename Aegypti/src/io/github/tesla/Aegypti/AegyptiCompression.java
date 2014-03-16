package io.github.tesla.Aegypti;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class AegyptiCompression {
	private JarOutputStream target;
	
	private void add(File source, JarOutputStream target) throws IOException
    {
      BufferedInputStream in = null;
      try
      {
    	  // If the file provided is a directory
        if (source.isDirectory())
        {
        	// Replace \ with File.separator 
          String name = source.getPath().replace("\\", File.separator);
         // If name is not empty
          if (!name.isEmpty())
          {
        	  // Remove the first for characters (tmp+File.separator)
            name = name.substring(4);
            // If name doesn't end with File.separator
            if (!name.endsWith(File.separator))
            	// Append File.separator to name
              name += File.separator;
            // Create a new entry with the name stored int the name variable
            JarEntry entry = new JarEntry(name);
            // Set the last modified time
            entry.setTime(source.lastModified());
            // Add the entry to the JarOutputStream
            target.putNextEntry(entry);
            // This bit of code is not actually necessary because putNextEntry() actually calls closeEntry()
            //target.closeEntry();
          }
          // Iterate through the files
          for (File nestedFile: source.listFiles())
        	 // Call add function on new file (we operate recursively on directories)
            add(nestedFile, target);
        }
        // (The file is not a folder) Create a new entry with the name
        JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").substring(4));
        // Set the last modified time
        entry.setTime(source.lastModified());
        // Add the entry to the JarOutputStream
        target.putNextEntry(entry);
        // Create a new BufferedInputStream using the JarOutputStream
        in = new BufferedInputStream(new FileInputStream(source));
        // Create a byte array to store data
        byte[] buffer = new byte[1024];
        // Loop forever
        while (true)
        {
        // Count is the bytes read 
          int count = in.read(buffer);
          // If count is -1 we're done reading
          if (count == -1)
        	  // Break the loop
            break;
          // Write th buffer to the JarOutputStream
          target.write(buffer, 0, count);
        }
        // Close the entry
        target.closeEntry();
      }
      finally
      {
    	  // If the BufferedInputStream is not null, close it
        if (in != null)
          in.close();
      }
    }
	public AegyptiCompression(String outpath, Manifest manifest) {
		try {
			target = new JarOutputStream(new FileOutputStream("output.jar"), manifest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void compress(File[] files) {
		try {
//			File[] files = new File("tmp").listFiles();
			for (File file : files) {
				try {
					System.out.println("[*] Archiving: "+file.getName());
					if (file.isDirectory()) { compress(file.listFiles()); return; }
					add(file, target);
				}
				catch (java.util.zip.ZipException e3) {
					System.out.println("Ignoring "+file.getName()+" to avoid duplicate entry errors");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			target.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
