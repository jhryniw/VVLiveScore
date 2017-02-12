package ca.ftcalberta.vvlivescore;

/**
 * Created by James on 2016-10-25.
 */
 enum Alliance {
    RED,
    BLUE,
    NONE;

    @Override
    public String toString() {
        switch (this) {
            case RED:
                return "Red";
            case BLUE:
                return "Blue";
            default:
                return super.toString();
        }
    }
}
