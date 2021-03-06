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

    // HashMap Idea Key/Package, Conflicts
    HashMap<String, String> PackageConflicts = new HashMap<String, String>();
    // HashMap Idea Key/Package, Dependancies
    HashMap<String, String> PackageDependancies = new HashMap<String, String>();

    // ArrayList of valid packages.
    ArrayList<String> ValidPackages = new ArrayList<String>();

    // Package names.
    ArrayList<String> PackageNames = new ArrayList<String>();

    // Package conflicts.
    ArrayList<String> PackageCons = new ArrayList<String>();	
    // Package dependancies.
    ArrayList<String> PackageDeps = new ArrayList<String>();	
    // Package dependancies list of lists.
    List<List> listList = new ArrayList<List>();	  
	  
    // Counting index for finding versions.
    ArrayList<Integer> IndexCount = new ArrayList<Integer>();

    // Arraylist of constraints.
    ArrayList<String> Constraints = new ArrayList<String>();

    // Arraylist of constraints needing to be installed.
    ArrayList<String> PositiveCons = new ArrayList<String>();

    // ArrayList of constraints that should never be installed.
    ArrayList<String> NegativeCons = new ArrayList<String>();
	  
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
    String CommandOutput = "";
    
    String packageName = "";
    String packageVersion = "";
    String currentPackage = "";
	 
	  
    boolean finished = false;
	  
    for (Package p : repo) 
    {
      packageName = p.getName();
      packageVersion = p.getVersion();
      Packages.add(packageName + "=" + packageVersion);
      PackageNames.add(packageName);
      
      //System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
	    
      for (List<String> clause : p.getDepends()) 
      {
	PackageDeps = new ArrayList<String>();
        //System.out.printf("  deps:");
        for (String q : clause) 
	{
	   if(q.equals(null))
	   {
	       PackageDeps.add("*");
	   }
           PackageDependancies.put(packageName + "=" + packageVersion, q);
	   PackageDeps.add(q);
           //System.out.printf(" %s", q);
        }
	//depsBuilder = "";
	//PackageDeps.add(">");
	listList.add(PackageDeps);
	//PackageDeps.clear();
        //System.out.println(PackageDeps);
        //System.out.println(PackageDep);
	//System.out.println(listList);	
        //System.out.printf("\n");
      }
      
      String conflictsSeparated = String.join(",", p.getConflicts());
	    
      if(conflictsSeparated.length() > 0)
      {
	      PackageConflicts.put(packageName + "=" + packageVersion, conflictsSeparated);
	      //System.out.printf("  cons:");
      	      //System.out.printf(" %s", conflictsSeparated);
              //System.out.printf("\n");
      }
	    
      if(conflictsSeparated.length() == 0)
      {
	      //System.out.printf("  cons:");
      	      //System.out.printf(" empty");
              //System.out.printf("\n");
      }
    }
	  
	  
    // USED TO SORT OUT INITIAL
    // IF INITIAL HAS ELEMENTS, COMPARE THESE TO PACKAGES, IF THEY EXIST, ADD TO INSTALLED PACKAGES.
    if(initial.size() > 0)
    {
	for(int i = 0; i < initial.size(); i++)
	{
	    String initialFind = initial.get(i);
	    if(Packages.contains(initialFind))
	    {
	        InstalledPackages.add(initialFind);
	    }
	}
    }
	  
    // USED TO SORT CONSTRAINTS INTO POSITIVE OR NEGATIVE (+ OR -)
    // REMINDER: + MEANS ONE OF THESE MUST BE INSTALLED.
    //           - MEANS NONE OF THESE MUST BE INSTALLED.
    for(int i = 0; i < constraints.size();)
        {
            String tempCon = "Null";
            tempCon = constraints.get(i);
            
            char charCheck = 'a';
            String putTogether = "";
            // initialise to name
            String currentCheck = "name";
            if (tempCon.charAt(0) == '+')
            {
                for (int j = 1; j < tempCon.length();)
                {
                    charCheck = tempCon.charAt(j);
                    // currentCheck needed to prevent illegal combinations or usage of name, operators, or version.
                    if(charCheck >= 'a' && charCheck <= 'z' 
                    || charCheck >= 'A' && charCheck <= 'Z')
                    {
                        putTogether += (Character.toString(charCheck));
                        currentCheck = "name";
                        // goes through the name of the package and sets it into putTogether which will give details for the package.
                    }
                    j++;
                }
                PositiveCons.add(putTogether);
                putTogether = "";
            }
            if (tempCon.charAt(0) == '-')
            {
                for (int j = 1; j < tempCon.length();)
                {
                    charCheck = tempCon.charAt(j);
                    // currentCheck needed to prevent illegal combinations or usage of name, operators, or version.
                    if(charCheck >= 'a' && charCheck <= 'z' 
                    || charCheck >= 'A' && charCheck <= 'Z')
                    {
                        putTogether += (Character.toString(charCheck));
                        currentCheck = "name";
                        // goes through the name of the package and sets it into putTogether which will give details for the package.
                    }
                    j++;
                }
                NegativeCons.add(putTogether);
                putTogether = "";
            }
            i++;
        }
	
   // Used for finding positive constraints.
   String posConFind = "";
   // Counter.
   int Counter = 0;
   // Used to find index.
   int Index = 0;	
	  
   if(PositiveCons.size() > 0)
   {
	for(int j = 0; j < PositiveCons.size(); j++)
	{
		posConFind = PositiveCons.get(j);
		for (int i = 0; i < PackageNames.size(); i++) 
                {
                   String pacName = PackageNames.get(i);
                   if(posConFind.equals(pacName))
                   {
                        //System.out.println("Found: " + posConFind + " at index: "+ Index);
                        IndexCount.add(Index);
			Counter ++;
                        Index ++;
                    }
                 }
        // pacIndex is current package we have at index 0. (took out better parts due to time limit).
        if (IndexCount.size() > 0) 
        {
            int pacIndex = IndexCount.get(0);
            //System.out.println("Package: " + Packages.get(pacIndex));
	    currentPackage = Packages.get(pacIndex);
        }
		//System.out.println(Counter);
		//System.out.println(IndexCount);
     }
	   

     if(!IndexCount.isEmpty())
	{
	    for (int a = 0; a < IndexCount.size(); a++) 
            {
                int pacIndex = IndexCount.get(a);
                //PackageDependancies.get(pacIndex);
		//System.out.println(pacIndex);
		//System.out.println(Packages.get(pacIndex));
		String indexPackage = Packages.get(pacIndex);
		String packDep = PackageDependancies.get(indexPackage);
		    
		if(PackageDependancies.get(indexPackage) == null)
		{
			finished = true;

			InstalledPackages.add(indexPackage);
			CommandOutput = "";

			CommandOutput += '"';
			CommandOutput += '+';
			CommandOutput += indexPackage;
			CommandOutput += '"';
			CommandList.add(CommandOutput);
			System.out.println(CommandList);
		}
            }
	}
     
}
	  
     public int solveIndex(String currentPackage)
     {
        for (int i = 0; i < PackageNames.size(); i++) 
	{
	   posConFind = PackageDependancies.get(currentPackage)
	   pacName = PackageNames.get(i);
	   if(posConFind.equals(pacName))
	   {
		//System.out.println("Found: " + posConFind + " at index: "+ Index);
		IndexCount.add(Index);
		Counter ++;
		Index ++;
	    }
	    if (IndexCount.size() > 0) 
            {
            	pacIndex = IndexCount.get(0);
            	//System.out.println("Package: " + Packages.get(pacIndex));
	    	currentPackage = Packages.get(pacIndex);
            }
	 }
     }
	  
     public String solvePackage(String currentPackage)
     {
        if(PackageDependancies.get(currentPackage))
	{
		PossiblePath.push('"' + '+' + currentPackage + '"');
	}
     }
	  
     /*   
      *   The idea was to try and follow what I list below in comments. (The very bottom).
      *   I really struggled to utilise github so ended up doing most of my work on paper and Eclipse to test things.
      *   Ontop of this, I have been cripplingly sick and have struggled to work on this project, despite the hours I have put into it.
      *   I managed to produce over 40 pieces of paper detailing specifics of how it could work, with all the fringe cases, but in the end I was unable to translate it here.
      */
	  
	  
	  
      //System.out.println(Packages);
      //System.out.println(PackageNames);	    
      //System.out.printf("  cons:");
      //System.out.printf(" %s", conflictsSeparated);
      //System.out.printf("\n");
      //System.out.print(CommandList);
      //System.out.println(constraints);
      //System.out.println(PositiveCons);
      //System.out.println(NegativeCons);
      //System.out.println(IndexCount);
      //System.out.print(ValidPackages);   
      //System.out.println(PackageConflicts);
      //System.out.println(PackageDependancies);
      //System.out.println(PackageDeps);
      //System.out.println(PackageDependancies.get("A=1"));
  }

  static String readFile(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    StringBuilder sb = new StringBuilder();
    br.lines().forEach(line -> sb.append(line));
    return sb.toString();
  }
}

//Initial population is empty, skip.

//Constraints Found

//PositiveCons found.
//PositiveCon A

//No NegativeCons found.

//Find all the packages with name A
//1 package found, A=1

//Package A=1 has dependancies
//B
//C
//Package A=1 has no conflicts

//Add A to current path
//Add "+A=1" to stack.

//Try to install B=2
//B=2 has no conflicts
//B=2 has dependancies
//C
//D
//Add "+B=2" to stack.

//Try to install C=3
//C=3 has no conflicts
//C=3 has dependancies
//D
//E
//Add "+C=3" to stack.

//Try to install D=0
//D=0 has no conflicts
//D=0 has dependancies
//E
//F
//Add "+D=0" to stack.

//Try to install E=8
//E=8 has no conflicts
//E=8 has conflicts.
//Is F installed in installedPackages? No.
//Is F inside the stack? No.
//E=8 has dependancies
//G
//Add "+E=8" to stack.

//Try to install G
//G=7 has no conflicts
//G=7 has dependancies
//I
//Add "+G=7" to stack.

//Try to install I
//I=9 has no conflicts
//I=9 has no dependancies
//I may be installed, add "+I=9" to stack
//Output Stack into CommandsList

