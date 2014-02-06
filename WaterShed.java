// Water Shed program for EnSoft
// Written by Sam Kruger

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class WaterShed {
    
    private String[] avoidArray, pegStartArray, samStartArray, riverSegArray, segmentStart, segmentFinish;                   
    private List<String> buildingPath = new ArrayList<String>();
    private List<String> successfulPath = new ArrayList<String>();

    public static void main(String[] args) {
        List<String> path = new ArrayList<String>();
        
        WaterShed ws = new WaterShed();
        ws.breakInputToArrays();
        ws.removeSegments();
        path = ws.recurseArrays();
        
        ws.printResult(path);
    }
    
    public void breakInputToArrays(){
        List<String> riverSegList = new ArrayList<String>();
        Scanner input = new Scanner(System.in);

        //Get Map: off top of input
        String next = input.nextLine();;
        
        //Get river segments input and put into a list
        while(!next.equals("Avoid:")){
            next = input.nextLine();
            riverSegList.add(next);
        }

        //Remove String Avoid: off the river segment list
        riverSegList.remove(riverSegList.size()-1);

        //Get input of river junctions to avoid
        String avoid = input.nextLine();

        //Get input of Peggy's Start Locations 
        String pegStart = input.nextLine(); //String Peggy:
        pegStart = input.nextLine();

        //Get input of Sam's Start Locations
        String samStart = input.nextLine(); //String Sam:
        samStart = input.nextLine();
        
        //Make the River Segment, Avoid, Peggy Start, and Sam Start lists into Arrays
        riverSegArray = riverSegList.toArray(new String[riverSegList.size()]);
        avoidArray = avoid.split("\\s");
        pegStartArray = pegStart.split("\\s");
        samStartArray = samStart.split("\\s");
    }
    
    //Method to remove River segments that contain the junctions to avoid
    //and split remaining River segments into lists of start and finish locations
    public void removeSegments(){
        List<String> splitStart = new ArrayList<String>();
        List<String> splitFinish = new ArrayList<String>();
        for(String preSplit: riverSegArray){
            Boolean found = false;
            String [] postSplit = preSplit.split("\\s");
            for(String address : avoidArray){
                if((postSplit[0].equals(address)) || (postSplit[1].equals(address))){
                found = true;
                break;
                }
            }
            if(!found){
                splitStart.add(postSplit[0]);
                splitFinish.add(postSplit[1]);
            }
        }
        segmentStart = splitStart.toArray(new String[splitStart.size()]);
        segmentFinish = splitFinish.toArray(new String[splitFinish.size()]);
    }
    
    //Method to check if there is a solution, if so sort, remove duplicates and print it
    public void printResult(List<String> solution){
        if (solution.equals("")){
            System.exit(1);
        } else {
            HashSet<String> hashSolution = new HashSet<String>();
            hashSolution.addAll(solution);
            solution.clear();
            solution.addAll(hashSolution);
            Collections.sort(solution);
            for(String locations: solution){
                System.out.println(locations);
            }
        }
    }

    //Method for checking if an element in the list is already there 2 times, thus allowing only one cycle
    public boolean onlyTwo(String start, List<String> path){
        int howMany = 0;
        for(int i=0;i<path.size();i++){
            if(start.equals(path.get(i))){
            howMany++;
            }
        }
        if(howMany<2){
            return true;
        }
        else return false;
    }

    
    //Method to find all successful paths between Peggy and Sam's Start Locations
    public List<String> findPath(String start, String finish){
        boolean multiple = onlyTwo(start, buildingPath);
        if(multiple){
            for (int i = 0; i < segmentStart.length;i++){
                if(start.equals(segmentStart[i]) && finish.equals(segmentFinish[i])){
                    buildingPath.add(start);
                    buildingPath.add(finish);
                    successfulPath.addAll(buildingPath);
                    buildingPath.remove(buildingPath.size()-1);
                    buildingPath.remove(buildingPath.size()-1);
                } else if(start.equals(segmentStart[i])) {
                    buildingPath.add(start);
                    findPath(segmentFinish[i], finish);
                }
            
                if(!buildingPath.isEmpty() && (i == (segmentStart.length - 1))){
                    buildingPath.remove(buildingPath.size()-1);
                }
            }
        } else{
            buildingPath.remove(buildingPath.size()-1);
        }
        return successfulPath;
    }

    public List<String> recurseArrays(){
        List<String> path = new ArrayList<String>();
        for (String i: pegStartArray){
            for (String j: samStartArray){
                path = findPath(i,j);
            }
        }
        return path;
    }  
}
