package com.example.dashborad_pipe.Repo;

import com.example.dashborad_pipe.Model.CoordinateGantt;
import com.example.dashborad_pipe.Model.DataModel;
import com.example.dashborad_pipe.entities.Phase;
import com.example.dashborad_pipe.entities.Taskphase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.*;

@SpringBootTest
class TaskphaseRepoTest {


    private TaskphaseRepo taskphaseRepo;
    private ProjetRepository projetRepository;
    private PhaseRepository phaseRepository;

    @Autowired
    TaskphaseRepoTest(TaskphaseRepo taskphaseRepo, ProjetRepository projetRepository, PhaseRepository phaseRepository) {
        this.taskphaseRepo = taskphaseRepo;
        this.projetRepository = projetRepository;
        this.phaseRepository = phaseRepository;
    }
//6,10
//    8,9
    @Test
    void save(){
        List<String> pp = new ArrayList<>();
pp.add("2");

       Optional<Taskphase> t =  taskphaseRepo.findById(3L);
        System.out.println(t);
        t.get().setPredecessors(pp);
         taskphaseRepo.save(t.get());

    }


    @Test
    void save2(){
        List<Taskphase> t =  taskphaseRepo.findAll();
        t.forEach(System.out::println);
        HashSet<Task> allTasks = new HashSet<Task>();
        for(Taskphase tt :t){

        System.out.println(tt);
        }



    }


    public static class Task{
        //the actual cost of the task
        public int cost;
        //the cost of the task along the critical path
        public int criticalCost;
        //a name for the task for printing
        public String name;
        //the tasks on which this task is dependant
        public HashSet<Task> dependencies = new HashSet<Task>();
        public Task(String name, int cost, Task... dependencies) {
            this.name = name;
            this.cost = cost;
            for(Task t : dependencies){
                this.dependencies.add(t);
            }
        }
        @Override
        public String toString() {
            return name+": "+criticalCost;
        }
        public boolean isDependent(Task t){
            //is t a direct dependency?
            if(dependencies.contains(t)){
                return true;
            }
            //is t an indirect dependency
            for(Task dep : dependencies){
                if(dep.isDependent(t)){
                    return true;
                }
            }
            return false;
        }
    }

    public static Task[] criticalPath(Set<Task> tasks){
        //tasks whose critical cost has been calculated
        HashSet<Task> completed = new HashSet<Task>();
        //tasks whose ciritcal cost needs to be calculated
        HashSet<Task> remaining = new HashSet<Task>(tasks);

        //Backflow algorithm
        //while there are tasks whose critical cost isn't calculated.
        while(!remaining.isEmpty()){
            boolean progress = false;

            //find a new task to calculate
            for(Iterator<Task> it = remaining.iterator();it.hasNext();){
                Task task = it.next();
                if(completed.containsAll(task.dependencies)){
                    //all dependencies calculated, critical cost is max dependency
                    //critical cost, plus our cost
                    int critical = 0;
                    for(Task t : task.dependencies){
                        if(t.criticalCost > critical){
                            critical = t.criticalCost;
                        }
                    }
                    task.criticalCost = critical+task.cost;
                    //set task as calculated an remove
                    completed.add(task);
                    it.remove();
                    //note we are making progress
                    progress = true;
                }
            }
            //If we haven't made any progress then a cycle must exist in
            //the graph and we wont be able to calculate the critical path
            if(!progress) throw new RuntimeException("Cyclic dependency, algorithm stopped!");
        }

        //get the tasks
        Task[] ret = completed.toArray(new Task[0]);
        //create a priority list
        Arrays.sort(ret, new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                //sort by cost
                int i= o2.criticalCost-o1.criticalCost;
                if(i != 0)return i;

                //using dependency as a tie breaker
                //note if a is dependent on b then
                //critical cost a must be >= critical cost of b
                if(o1.isDependent(o2))return -1;
                if(o2.isDependent(o1))return 1;
                return 0;
            }
        });

        return ret;
    }

}