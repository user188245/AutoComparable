package auto.util;

public class TreePositionCorrectorFactory {

    public static TreePositionCorrector instance(){
        return new TreePositionCorrectorImpl(new TreePositionCorrectorImpl.PositionWriter(0));
    }

}
