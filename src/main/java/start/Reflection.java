package start;

import java.lang.reflect.Field;

/**
 * A utility class for retrieving properties of objects using reflection.
 */
public class Reflection {

    /**
     * Retrieves properties of the specified object using reflection and prints them to the console.
     *
     * @param object The object whose properties need to be retrieved.
     */
    public static void retrieveProperties(Object object) {

        // Iterate through all fields of the object
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Set the field to accessible to avoid IllegalAccessException

            // Retrieve the value of the field
            Object value;
            try {
                value = field.get(object);
                // Print the name of the field and its value
                System.out.println(field.getName() + "=" + value);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
