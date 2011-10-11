package Movement;

import lejos.nxt.*;
import lejos.robotics.proposal.DifferentialPilot;

public class PilotMovement implements IMovement {

    public static final int SPEED = 400;
    public DifferentialPilot pilot = new DifferentialPilot(175, 113, Motor.C, Motor.B);

    /**
     * Zorgt ervoor dat de robot rechtdoor rijdt over een gekozen afstand.
     *
     * @param distance
     * 		Afstand die moet worden afgelegd. Negatieve afstand om achteruit te rijden. (mm)
     */
    public void MoveStraight(double distance) {
        pilot.setSpeed(SPEED);
        pilot.travel((float)distance, true);
    }

    /**
     * Laat de robot terplekke draaien.
     *
     * @param angle
     * 		Hoek die de robot moet afleggen. Negatieve hoek om met de klok te draaien.
     */
    public void TurnOnSpotCCW(double angle) {
        pilot.setSpeed(SPEED);
        double hoek = angle * (-1);	// positive angle rotates clockwise
        pilot.travel((float)hoek, true);
    }

    public void TurnAroundWheel(double angle, boolean isLeft) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
