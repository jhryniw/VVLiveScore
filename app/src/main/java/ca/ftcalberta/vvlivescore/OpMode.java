package ca.ftcalberta.vvlivescore;

/**
 * Created by James on 2016-12-28.
 */

enum OpMode {
    AUTONOMOUS,
    TELEOP;

    @Override
    public String toString() {
        switch (this) {
            case AUTONOMOUS:
                return "Autonomous";
            case TELEOP:
                return "Teleop";
            default:
                return super.toString();
        }
    }
}
