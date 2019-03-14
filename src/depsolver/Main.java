package depsolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
    
      // ArryaList for holding list of commands used during run of program.
    ArrayList<String> CommandList = new ArrayList<String>();
      // Could perhaps remove these ArrayLists and work from constraints
      // Arraylist of constraints needing to be installed.
    ArrayList<String> PositiveCon = new ArrayList<String>();
      // ArrayList of constraints that should never be installed.
    ArrayList<String> NegativeCon = new ArrayList<String>();
      // ArrayList of valid packages. // ADDED
    ArrayList<String> ValidPackages = new ArrayList<String>();
	  
    // HashMap Idea Key/Package, Dependancies
    HashMap<String, String> PackageConflicts = new HashMap<String, String>();
    
    // For loop to add constraints into positive or negative ArrayList
    for(int i = 0; i < constraints.size();)
        {
            String tempCon = "Null";
            tempCon = constraints.get(i);
            if (tempCon.charAt(0) == '+')
            {
                PositiveCon.add(Character.toString(tempCon.charAt(1)));
            }
            if (tempCon.charAt(0) == '-')
            {
                NegativeCon.add(Character.toString(tempCon.charAt(1)));
            }
            i++;
        }
    
    // Get an element from positive con, see if it exists in repository, install (Will skip if empty)
    // Doesn't take into account different versions, just finds if the name matches. (Name currently only consists of one letter, fix)
    
    for (Package p : repo) 
    {
      String tempPos = "Null";
      tempPos = "A";
      String currentCon = p.getName();
      
      // ADDED
      String packageName = p.getName();
      String packageVersion = p.getVersion();
      ValidPackages.add(packageName + "=" + packageVersion);
      
      System.out.println(currentCon);
      System.out.println(tempPos);
      
      if(currentCon.equals("A"))
        {
          String CommandOutput = "";
        
          CommandOutput += '"';
          CommandOutput += '+';
          CommandOutput += p.getName();
          CommandOutput += '=';
          CommandOutput += p.getVersion();
          CommandOutput += '"';
          
          CommandList.add(CommandOutput);
        }
      
      System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
	    
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
	      PackageConflicts.put(packageName + "=" + packageVersion, ": " + conflictsSeparated);
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
	    
      //System.out.printf("  cons:");
      //System.out.printf(" %s", conflictsSeparated);
      //System.out.printf("\n");
      
      System.out.print(CommandList);
      System.out.print(PositiveCon);
      System.out.print(NegativeCon);
      System.out.print(ValidPackages);
	    
	    System.out.print(PackageConflicts);
    }
  }

  static String readFile(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    StringBuilder sb = new StringBuilder();
    br.lines().forEach(line -> sb.append(line));
    return sb.toString();
  }
}
