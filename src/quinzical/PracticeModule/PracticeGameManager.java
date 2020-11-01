package quinzical.PracticeModule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The game manager for practice module which is used to load
 * all the categories to be displayed in the drop down menu
 *
 * @author Danil Linkov
 */
public class PracticeGameManager {

    // Having a static instance of this game manager so that other controllers can access this
    private static PracticeGameManager instance;
    // String list of all the categories in the categories folder
    private ArrayList<String> categories;

    public PracticeGameManager() {
        instance = this;
    }

    /**
     * Returns the currently used instance of this class.
     * @return The currently used instance of PracticeGameManager
     */
    public static PracticeGameManager getInstance() {
        return instance;
    }

    /**
     * Get all the categories
     * @return A string list of categories.
     */
    public ArrayList<String> getCategories() {
        return categories;
    }

    /**
     * Load a list of all the categories in the categories folder
     * @param location the location of the category files; essentially game type.
     */
    public void loadAllCategories(String location) {
        // Used to store all the file paths in the folder
        ArrayList<String> filePaths;

        // Getting the categories folder path
        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath+="/categories/"+location;
        // Creating a file object based on the path
        File categoriesFolder = new File(categoriesPath);

        // Getting all the file paths in that folder
        filePaths = new ArrayList(Arrays.asList(categoriesFolder.list()));
        categories = new ArrayList<>();

        // Adding them to the list
        for (String filePath : filePaths) {
            categories.add(filePath.replace(".txt", ""));
        }
    }

}
