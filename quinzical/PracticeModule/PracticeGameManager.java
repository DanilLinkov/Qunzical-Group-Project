package quinzical.PracticeModule;

import quinzical.Questions.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PracticeGameManager {

    private static PracticeGameManager _instance;
    private ArrayList<String> _categories;

    public PracticeGameManager() {
        _instance = this;
        loadAllCategories();
    }

    public static PracticeGameManager getInstance() {
        return _instance;
    }

    public ArrayList<String> getCategories() {
        return _categories;
    }

    private void loadAllCategories() {
        ArrayList<String> filePaths;

        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath+="/categories";
        File categoriesFolder = new File(categoriesPath);

        filePaths = new ArrayList(Arrays.asList(categoriesFolder.list()));
        _categories = new ArrayList<>();

        for (String filePath : filePaths) {
            _categories.add(filePath.replace(".txt", ""));
        }
    }

}
