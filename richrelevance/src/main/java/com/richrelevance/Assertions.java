package com.richrelevance;


/**
 * Class which helps with assertions
 */
public class Assertions {

    /**
     * Checks if the given object is null, and handles assertion failure with the message if it is.
     * @param message The message to log to use if the object is null.
     * @param obj The object to check.
     * @return True if the object existed, false if it was null.
     */
    public static boolean assertNotNull(String message, Object obj) {
        if (obj == null) {
            handleAssertionFailure(message);
            return false;
        }

        return true;
    }

    /**
     * Performs the action defined for assertion failures using the given message.
     * @param message A message about the failure.
     */
    public static void handleAssertionFailure(String message) {
        AssertionError error = new AssertionError(message);

        RRLog.e("Assertion Failure", message, error);
    }
}
