package penoplatinum.movement;

public interface IMovement {
    void MoveStraight(double distance);
    void MoveStraight(double distance, boolean block);
    void TurnOnSpotCCW(double angle);
    void TurnOnSpotCCW(double angle, boolean block);
    void TurnAroundWheel(double angle, boolean isLeft);
    void Stop();
    boolean isStopped();
    //void Calibrate
}