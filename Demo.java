import java.util.*;

class Process {
    int id;
    int arrivalTime;
    int serviceTime;
    int priority;
    int remainingServiceTime;
    int startTime = -1; // 프로세스 시작 시간, 초기값은 -1로 설정
    int finishTime = 0; // 프로세스 완료 시간

    public Process(int id, int arrivalTime, int serviceTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.priority = priority;
        this.remainingServiceTime = serviceTime;
    }
}

public class Demo {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of processes:");
        int numberOfProcesses = scanner.nextInt();

        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("Enter process " + (i + 1) + " ID, arrival time, service time, priority:");
            int id = scanner.nextInt();
            int arrivalTime = scanner.nextInt();
            int serviceTime = scanner.nextInt();
            int priority = scanner.nextInt();
            processes.add(new Process(id, arrivalTime, serviceTime, priority));
        }

        scheduleProcesses(processes);
    }

    private static void scheduleProcesses(List<Process> processes) {
        int currentTime = 0;
        List<Process> completedProcesses = new ArrayList<>();
        PriorityQueue<Process> processQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingServiceTime));

        while (completedProcesses.size() < processes.size()) {
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && !processQueue.contains(process) && !completedProcesses.contains(process)) {
                    processQueue.add(process);
                }
            }

            if (!processQueue.isEmpty()) {
                Process currentProcess = processQueue.poll();

                if (currentProcess.startTime == -1) {
                    currentProcess.startTime = currentTime;
                }

                currentProcess.remainingServiceTime--;
                currentTime++;

                if (currentProcess.remainingServiceTime == 0) {
                    currentProcess.finishTime = currentTime;
                    completedProcesses.add(currentProcess);
                } else {
                    processQueue.add(currentProcess);
                }
            } else {
                currentTime++;
            }
        }

        printResults(completedProcesses);
    }

    private static void printResults(List<Process> completedProcesses) {
        int totalWaitingTime = 0, totalResponseTime = 0, totalTurnAroundTime = 0;

        System.out.println("Process ID\tWaiting Time\tResponse Time\tTurnaround Time");
        for (Process process : completedProcesses) {
            int waitingTime = process.finishTime - process.arrivalTime - process.serviceTime;
            int responseTime = process.startTime - process.arrivalTime;
            int turnAroundTime = process.finishTime - process.arrivalTime;

            System.out.println(process.id + "\t\t\t" + waitingTime + "\t\t\t" + responseTime + "\t\t\t" + turnAroundTime);

            totalWaitingTime += waitingTime;
            totalResponseTime += responseTime;
            totalTurnAroundTime += turnAroundTime;
        }

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / completedProcesses.size());
        System.out.println("Average Response Time: " + (double) totalResponseTime / completedProcesses.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnAroundTime / completedProcesses.size());
    }
}
