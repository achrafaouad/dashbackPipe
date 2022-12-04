package com.example.dashborad_pipe.services;

import com.example.dashborad_pipe.Model.CoordinateGantt;
import com.example.dashborad_pipe.Model.CriticalPath;
import com.example.dashborad_pipe.Model.DataModel;
import com.example.dashborad_pipe.Model.TaskModel;
import com.example.dashborad_pipe.Repo.PhaseRepository;
import com.example.dashborad_pipe.Repo.ProjetRepository;
import com.example.dashborad_pipe.Repo.TaskphaseRepo;
import com.example.dashborad_pipe.entities.Phase;
import com.example.dashborad_pipe.entities.Taskphase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@org.springframework.stereotype.Service
public class Service {
    private ProjetRepository projetRepository;
    private PhaseRepository phaseRepository;
    private TaskphaseRepo taskphaseRepo;
    @Autowired
    public Service(ProjetRepository projetRepository, PhaseRepository phaseRepository, TaskphaseRepo taskphaseRepo) {
        this.projetRepository = projetRepository;
        this.phaseRepository = phaseRepository;
        this.taskphaseRepo = taskphaseRepo;
    }


    public List<Map<String,?>> getprojectPhases(){
        List<Map<String,?>> ls =  phaseRepository.getprojectPhases();
        ls.forEach(System.out::println);
        System.out.println("hello world");
        return ls;
    }

    public List<Taskphase> alltasks(){
        List<Taskphase>  ls =  taskphaseRepo.findAll();
        ls.forEach(System.out::println);
        System.out.println("hello world");
        return ls;
    }
    public Integer spliteF(Integer id, Double pourcent ){
        Integer ls =  phaseRepository.spliteF(id, pourcent);

        System.out.println("hello world");
        return ls;
    }

    public void addTask(TaskModel m){
        Phase p = phaseRepository.findPhaseById(m.getPhase());
        taskphaseRepo.save(Taskphase.builder().version(m.getVersion()).phase(p).date_depart(m.getDate_depart()).predecessors(m.getPred()).date_arrive(m.getDate_arrive()).id(m.getId()).name(m.getName()).build());
        System.out.println("hello world");
    }

    public List<DataModel>  getGantt(Integer id){
        List<Taskphase> p = this.taskphaseRepo.findAll();

        String array[] = new String[] {"init", "updated"};
        List<DataModel> dss = new ArrayList<>();

        for(String vers: array){
            DataModel ds = new DataModel();
            ds.setName(vers);
            for(Taskphase ph : p){
                if(ph.getPhase().getId() == id){
                    if(ph.getVersion().equals(vers)){
                        CoordinateGantt crrd = new CoordinateGantt();

                        crrd.setX(ph.getName());
                        crrd.setId(ph.getId().toString());
                        crrd.setY(List.of(ph.getDate_depart(),ph.getDate_arrive()));
                        crrd.setPedecessors(ph.getPredecessors());
                        ds.getData().add(crrd);
                    }
                }
            }
            dss.add(ds);
        }

        return dss;
    }

    public void updatephase(Phase m){
        phaseRepository.save(m);
        System.out.println("hello world");

    }



//    tasing

public static class Task{
    //the actual cost of the task
    public int cost;
    //the cost of the task along the critical path
    public int criticalCost;
    //a name for the task for printing
    public String name;
    //the tasks on which this task is dependant
    public HashSet<CriticalPath.Task> dependencies = new HashSet<CriticalPath.Task>();
    public Task(String name, int cost, CriticalPath.Task... dependencies) {
        this.name = name;
        this.cost = cost;
        for(CriticalPath.Task t : dependencies){
            this.dependencies.add(t);
        }
    }
    @Override
    public String toString() {
        return name+": "+criticalCost;
    }
    public boolean isDependent(CriticalPath.Task t){
        //is t a direct dependency?
        if(dependencies.contains(t)){
            return true;
        }
        //is t an indirect dependency
        for(CriticalPath.Task dep : dependencies){
            if(dep.isDependent(t)){
                return true;
            }
        }
        return false;
    }
}
    public static CriticalPath.Task[] criticalPath(Set<CriticalPath.Task> tasks){
        //tasks whose critical cost has been calculated
        HashSet<CriticalPath.Task> completed = new HashSet<CriticalPath.Task>();
        //tasks whose ciritcal cost needs to be calculated
        HashSet<CriticalPath.Task> remaining = new HashSet<CriticalPath.Task>(tasks);

        //Backflow algorithm
        //while there are tasks whose critical cost isn't calculated.
        while(!remaining.isEmpty()){
            boolean progress = false;

            //find a new task to calculate
            for(Iterator<CriticalPath.Task> it = remaining.iterator(); it.hasNext();){
                CriticalPath.Task task = it.next();
                if(completed.containsAll(task.dependencies)){
                    //all dependencies calculated, critical cost is max dependency
                    //critical cost, plus our cost
                    int critical = 0;
                    for(CriticalPath.Task t : task.dependencies){
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
        CriticalPath.Task[] ret = completed.toArray(new CriticalPath.Task[0]);
        //create a priority list
        Arrays.sort(ret, new Comparator<CriticalPath.Task>() {

            @Override
            public int compare(CriticalPath.Task o1, CriticalPath.Task o2) {
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
