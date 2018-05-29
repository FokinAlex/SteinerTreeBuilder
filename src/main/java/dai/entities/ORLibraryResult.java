package dai.entities;

public class ORLibraryResult extends ORLibraryTask {

    public final double weightMST;
    public final double weightSMT;

    public ORLibraryResult(int id, double weightMST, double weightSMT) {
        super(id);
        this.weightMST = weightMST;
        this.weightSMT = weightSMT;
    }
}
