package Movement;

public interface IMovement {
    void MoveStraight(double distance);
    void TurnOnSpotCCW(double angle);
    void TurnAroundWheel(double angle, boolean isLeft);
    void Stop();
    //void Calibrate
}