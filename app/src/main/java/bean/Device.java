package bean;

public class Device {
    private String MAC;
    private int singleStrench;

    public Device(String MAC, int singleStrench) {
        this.MAC = MAC;
        this.singleStrench = singleStrench;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public int getSingleStrench() {
        return singleStrench;
    }

    public void setSingleStrench(int singleStrench) {
        this.singleStrench = singleStrench;
    }
}
