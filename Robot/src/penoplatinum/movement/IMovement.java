package penoplatinum.movement;

public interface IMovement {
    void MoveStraight(double distance);
    void MoveStraight(double distance, boolean block);
    void TurnOnSpotCCW(double angle);
    void TurnAroundWheel(double angle, boolean isLeft);
    void Stop();
    //void Calibrate
}