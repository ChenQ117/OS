package processSchedue.dataStruct;

import java.util.ArrayList;
import java.util.List;

public class PCB {
    private String name;
    private int start;
    private int space;
    private PCB nextProcess;
    private List pageTable;//页表
    public PCB(){

    }

    public PCB(String name, int space) {
        this.name = name;
        this.space = space;
        pageTable = new ArrayList();
    }

    public PCB(String name, int start, int space, PCB nextProcess) {
        this.name = name;
        this.start = start;
        this.space = space;
        this.nextProcess = nextProcess;
        pageTable = new ArrayList();

    }

    public List getPageTable() {
        return pageTable;
    }

    public void setPageTable(List pageTable) {
        this.pageTable = pageTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public PCB getNextProcess() {
        return nextProcess;
    }

    public void setNextProcess(PCB nextProcess) {
        this.nextProcess = nextProcess;
    }

    @Override
    public String toString() {
        return "PCB{" +
                "进程名='" + name + '\'' +
                ", 进程大小=" + space +
                ", 页表=" + pageTable +
                '}';
    }
}
