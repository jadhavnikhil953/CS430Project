package job.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

//User-Defined Comparator to sort the jobs in ascending order of their end time
class CompareJobSchedule implements Comparator<Job>
{
	public int compare(Job a,Job b)
	{
		 if(a.end<b.end)
			 return -1;
		 else if(a.end>b.end)
			 return 1;
		 else
			 return 0;
	}

}

//Class to store the parameters related to the Job
//start: Stores the start time of the job
//end: Stores the end time of the job
//position: Stores the actual position of the job before sorting
//machineIndex: Stores the index of the machine to which the job has been assigned. Initially this value is 0;
class Job
{
	int start;
	int end;
	int position;
	int machineIndex;
	Job(int start, int end, int position)
	{
		this.start=start;
		this.end=end;
		this.position=position;
		this.machineIndex=0;
	}
	@Override
	public String toString() {
		return "Job [start=" + start + ", end=" + end + ", position=" + position + ", machineIndex=" + machineIndex
				+ "]";
	}
	
}

//Class to store the parameters related to the Machine
//jobsAssigned: List of the jobs assigned to this machine
//index: This is used for keeping an index for each machine
class Machine
{
	ArrayList<Job> jobsAssigned = new ArrayList<Job>();
	int index;
	@Override
	public String toString() {
		return "Machine [jobsAssigned=" + jobsAssigned + ", index=" + index + "]";
	}
	public ArrayList<Job> getJobsAssigned() {
		return jobsAssigned;
	}
}


public class JobScheduler {

	//The Function that schedules the Maximum of n jobs on m machines
	public static ArrayList<Job> scheduleMaximumJobs(ArrayList<Job> jobList, int noOfMachines, ArrayList<Machine> machineList, 	ArrayList<Integer> jobsNotProcessed)
	{
		ArrayList<Job> result = new ArrayList<Job>();
		
		CompareJobSchedule compareJobSchedule = new CompareJobSchedule();
		//Sort the jobs in ascending order of their end time
		Collections.sort(jobList, compareJobSchedule);
		
		//Initialize Machine List which will be used to store the jobs assigned to each machine
		for(int i=0;i<noOfMachines;i++)
		{
			Machine machine = new Machine();
			machine.index=i;
			machineList.add(machine);
		}

		//We use this indicator to keep a track of the machine currently in use
		int machineInd=0;
		
		//Initialize an array machineLoad which will be used to compare the end time of an already assigned job on the machine
		//and the start time of the new job to be assigned, so that no two jobs overlap
		ArrayList<Integer> machineLoad = new ArrayList<Integer>();
	
		//Initially, as there is no job yet assigned to any of the Machines, we set the machineLoad as 0
		for(int i=0;i<noOfMachines;i++)
		{
			machineLoad.add(i, 0);
		}
		
		//Assign the job with the least end time to the first machine
		jobList.get(0).machineIndex=0;
				
		result.add(jobList.get(0));
		
		//Set the machine Load of the first machine equal to the end time of first job, as we assigned the first job to the first machine
		machineLoad.set(0, jobList.get(0).end);
		
		//Add the first job to the list of Jobs assigned of the first machine
		machineList.get(0).getJobsAssigned().add(jobList.get(0));

		
		for(int i=1;i< jobList.size();i++)
		{
	
			//Condition to check if there are any available machines
			if(jobList.get(i-1).machineIndex<noOfMachines-1)
			{
				//If yes, we assign the job to the next machine
				machineInd++;
				
				//Condition to check if the machine is free i.e. machineLoad = 0
				if(machineLoad.get(machineInd)==0)
				{
					jobList.get(i).machineIndex=machineInd;
					result.add(jobList.get(i));
					machineLoad.set(machineInd, jobList.get(i).end);
					machineList.get(machineInd).getJobsAssigned().add(jobList.get(i));
					
				}
				else
				{
					//If machine already has a job assigned to it, we check if the start time of this job is greater than
					//the machineLoad so that no two jobs overlap
					if(jobList.get(i).start>machineLoad.get(machineInd))
					{
						jobList.get(i).machineIndex=machineInd;
						result.add(jobList.get(i));
						machineLoad.set(machineInd,jobList.get(i).end);
						machineList.get(machineInd).getJobsAssigned().add(jobList.get(i));
					}
					else
						jobsNotProcessed.add(jobList.get(i).position);
				}
				
			}
			else 
			{
				machineInd = 0;
				if(jobList.get(i).start>machineLoad.get(machineInd))
				{
					jobList.get(i).machineIndex=machineInd;
					result.add(jobList.get(i));
					machineLoad.set(machineInd,jobList.get(i).end);
					machineList.get(machineInd).getJobsAssigned().add(jobList.get(i));
				}
				else
					jobsNotProcessed.add(jobList.get(i).position);
				
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) throws Exception
	{
		//File containing the Input
		//First line indicates the number of machines
		//Next each line denotes the start and end time of each job
		File file = new File("src/job/scheduler/Input.txt");
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			int line = 0;
			int noOfMachines = 0;
			ArrayList<Integer> start = new ArrayList<Integer>();
			ArrayList<Integer> end = new ArrayList<Integer>();
			ArrayList<Job> result = new ArrayList<Job>();
			ArrayList<Machine> machineList = new ArrayList<Machine>();
			ArrayList<Integer> jobsNotProcessed = new ArrayList<Integer>();
			String s;
			
			//Read the first line of the input file to get the number of Machines i.e. m
			while((s=bufferedReader.readLine())!=null)
			{
				line++;
				if(line==1)
				{
					 noOfMachines= Integer.parseInt(s);
				}
			}
			
			Scanner input = new Scanner(file);
			int count = 0;
			
			//Get the start time and end time of each job from the Input.txt file and store them
			//in the arrayList start and end respectively
			while(input.hasNext())
			{
				int word = Integer.parseInt(input.next());
				if(count!=0)
				{
					if(count%2==1)
					
					{
						start.add(word);
					}
					else
					{
						end.add(word);
					}
				}
					count++;
			}

			//Add the jobs to a list
			ArrayList<Job> job = new ArrayList<>();
			for(int i = 0; i < start.size(); i++)
			{
				job.add(new Job(start.get(i), end.get(i), i));
			}
			
			// Function call
			//Call the scheduleMaximumJob function
			result = scheduleMaximumJobs(job, noOfMachines, machineList, jobsNotProcessed);
			
			//Formatting the Output returned by the algorithm
			for(int i=0; i<machineList.size();i++)
			{
				System.out.println("Machine #"+(i+1)+ " has jobs:");
				for(int j=0;j<machineList.get(i).jobsAssigned.size();j++)
					System.out.println("("+machineList.get(i).jobsAssigned.get(j).start+","+machineList.get(i).jobsAssigned.get(j).end+")");
			}
			System.out.println("Jobs not processed:");
			for(int i=0;i<jobsNotProcessed.size();i++)
			{
				System.out.println("("+getJobByPosition(job,jobsNotProcessed.get(i)).start+","+getJobByPosition(job,jobsNotProcessed.get(i)).end+")");
			}
			
			input.close();
			bufferedReader.close();
			if(jobsNotProcessed.size()!=0)
				System.out.println(job.size()-jobsNotProcessed.size()+" number of jobs out of "+job.size()+" total jobs processed" );
			else
				System.out.println("All "+ job.size() + " jobs are processed");
		}
		catch (Exception e) {
			System.err.println(e);
		}

	}

	//Function to get the Job by its position. This is used to get the jobs that were not processed
	private static Job getJobByPosition(ArrayList<Job> jobList, Integer position) {
		Job job = new Job(0, 0, position);
		for(int i=0;i<jobList.size();i++)
		{
			if(jobList.get(i).position==position)
				job= jobList.get(i);
		}
		return job;
	}

}
