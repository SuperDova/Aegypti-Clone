package io.github.tesla.Aegypti;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class AegyptiCompiler {
	private String name;
	private String src;
	public AegyptiCompiler(String name, String src) {
		this.name = name;
		this.src = src;
	}
	public void writeFile() {
		System.out.println("[*] Writing to tmp java source file. . .");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(name+".java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.println(src);
		pw.close();
	}
	public void compile() {
		writeFile();
		System.out.println("[*] Compiling: "+src+" in file " +name+".java");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(new File(name+".java")));
		compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
		try {
			fileManager.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
}