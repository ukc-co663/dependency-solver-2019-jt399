package depsolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Stack;

class Package {
  private String name;
  private String version;
  private Integer size;
  private List<List<String>> depends = new ArrayList<>();
  private List<String> conflicts = new ArrayList<>();

  public String getName() { return name; }
  public String getVersion() { return version; }
  public Integer getSize() { return size; }
  public List<List<String>> getDepends() { return depends; }
  public List<String> getConflicts() { return conflicts; }
  public void setName(String name) { this.name = name; }
  public void setVersion(String version) { this.version = version; }
  public void setSize(Integer size) { this.size = size; }
  public void setDepends(List<List<String>> depends) { this.depends = depends; }
  public void setConflicts(List<String> conflicts) { this.conflicts = conflicts; }
}

public class Main {
  public static void main(String[] args) throws IOException {
    TypeReference<List<Package>> repoType = new TypeReference<List<Package>>() {};
    List<Package> repo = JSON.parseObject(readFile(args[0]), repoType);
    TypeReference<List<String>> strListType = new TypeReference<List<String>>() {};
    List<String> initial = JSON.parseObject(readFile(args[1]), strListType);
    List<String> constraints = JSON.parseObject(readFile(args[2]), strListType);

    // CHANGE CODE BELOW:
    // using repo, initial and constraints, compute a solution and print the answer
    
    // List of valid initial
    ArrayList<String> ValidInitial = new ArrayList<String>();
	  
    // List of packages.
    ArrayList<String> Packages = new ArrayList<String>();
    // length of packages.
    String PackageLength = Packages.toString();
	  
    // HashMap Idea Key/Package, Dependancies
    HashMap<String, String> PackageConflicts = new HashMap<String, String>();

    // ArrayList of valid packages.
    ArrayList<String> ValidPackages = new ArrayList<String>();

    // Package names.
    ArrayList<String> PackageNames = new ArrayList<String>();

    // Counting index for finding versions.
    ArrayList<Integer> IndexCount = new ArrayList<Integer>();

    // Arraylist of constraints.
    ArrayList<String> Constraints = new ArrayList<String>();

    // Arraylist of constraints needing to be installed.
    ArrayList<String> PositiveCon = new ArrayList<String>();

    // ArrayList of constraints that should never be installed.
    ArrayList<String> NegativeCon = new ArrayList<String>();
	  
    // List of installed packages
    ArrayList<String> InstalledPackages = new ArrayList<String>();
	  
    // Path currently taking.
    String CurrentPath = "Null";

    // Stack for commands taken.
    Stack<String> PossiblePath = new Stack<>();

    // Package names.
    ArrayList<String> PreviousPaths = new ArrayList<String>();

    // ArryaList for holding list of commands used during run of program.
    ArrayList<String> CommandList = new ArrayList<String>();
    
    for (Package p : repo) 
    {
      // ADDED
      String packageName = p.getName();
      String packageVersion = p.getVersion();
      Packages.add(packageName + "=" + packageVersion);
      PackageNames.add(packageName);
      
      //System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
	    
      for (List<String> clause : p.getDepends()) 
      {
        System.out.printf("  deps:");
        for (String q : clause) 
	{
        System.out.printf(" %s", q);
        }
        System.out.printf("\n");
      }
      
      String conflictsSeparated = String.join(",", p.getConflicts());
	    
      if(conflictsSeparated.length() > 0)
      {
	      PackageConflicts.put(packageName + "=" + packageVersion, conflictsSeparated);
	      System.out.printf("  cons:");
      	      System.out.printf(" %s", conflictsSeparated);
              System.out.printf("\n");
      }
	    
      if(conflictsSeparated.length() == 0)
      {
	      System.out.printf("  cons:");
      	      System.out.printf(" empty");
              System.out.printf("\n");
      }
    }
      System.out.print(Packages);
      System.out.print(PackageNames);	    
      //System.out.printf("  cons:");
      //System.out.printf(" %s", conflictsSeparated);
      //System.out.printf("\n");
      //System.out.print(CommandList);
      //System.out.print(PositiveCon);
      //System.out.print(NegativeCon);
      //System.out.print(ValidPackages);   
      //System.out.print(PackageConflicts);
      //System.out.println(PackageConflicts.get("B=3.2"));
  }

  static String readFile(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    StringBuilder sb = new StringBuilder();
    br.lines().forEach(line -> sb.append(line));
    return sb.toString();
  }
}
