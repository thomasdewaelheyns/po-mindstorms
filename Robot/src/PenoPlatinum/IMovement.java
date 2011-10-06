package PenoPlatinum;

/**
 *
 * @author MHGameWork
 */
public interface IMovement {
    void MoveStraight(double distance);
    void TurnOnSpotCCW(double angle);
    void TurnAroundWheel(double angle, boolean isLeft);
    //void Calibrate
}
