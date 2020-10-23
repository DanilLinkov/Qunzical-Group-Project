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
    private static PracticeGameManager _instance;
    // String list of all the categories in the categories folder
    private ArrayList<String> _categories;

    public PracticeGameManager() {
        _instance = this;
        // Loading all the categories
        loadAllCategories();
    }

    /**
     * Get this classes instance
     * @return
     */
    public static PracticeGameManager getInstance() {
        return _instance;
    }

    /**
     * Get all the categories
     * @return
     */
    public ArrayList<String> getCategories() {
        return _categories;
    }

    /**
     * Load a list of all the categories in the categories folder
     */
    private void loadAllCategories() {
        // Used to store all the file paths in the folder
        ArrayList<String> filePaths;

        // Getting the categories folder path
        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath+="/categories/NZ";
        // Creating a file object based on the path
        File categoriesFolder = new File(categoriesPath);

        // Getting all the file paths in that folder
        filePaths = new ArrayList(Arrays.asList(categoriesFolder.list()));
        _categories = new ArrayList<>();

        // Adding them to the list
        for (String filePath : filePaths) {
            _categories.add(filePath.replace(".txt", ""));
        }
    }

}
